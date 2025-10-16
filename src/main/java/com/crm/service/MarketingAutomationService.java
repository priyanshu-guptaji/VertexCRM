package com.crm.service;

import com.crm.entity.*;
import com.crm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for handling Marketing Automation features including:
 * - Campaign Management
 * - Contact Segmentation
 * - Drip Campaign Execution
 * - Campaign Analytics
 */
@Service
public class MarketingAutomationService {
    
    @Autowired
    private CampaignRepository campaignRepository;
    
    @Autowired
    private CampaignTemplateRepository campaignTemplateRepository;
    
    @Autowired
    private CampaignMetricsRepository campaignMetricsRepository;
    
    @Autowired
    private SegmentRepository segmentRepository;
    
    @Autowired
    private SegmentRuleRepository segmentRuleRepository;
    
    @Autowired
    private DripCampaignRepository dripCampaignRepository;
    
    @Autowired
    private DripCampaignStepRepository dripCampaignStepRepository;
    
    @Autowired
    private ContactRepository contactRepository;
    
    @Autowired
    private OrganizationRepository organizationRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    // ========== Campaign Management ==========
    
    /**
     * Initialize metrics for a campaign
     */
    @Transactional
    public CampaignMetrics initializeCampaignMetrics(Campaign campaign) {
        Optional<CampaignMetrics> existing = campaignMetricsRepository.findByCampaign(campaign);
        if (existing.isPresent()) {
            return existing.get();
        }
        
        CampaignMetrics metrics = new CampaignMetrics();
        metrics.setCampaign(campaign);
        metrics.setOrganization(campaign.getOrganization());
        return campaignMetricsRepository.save(metrics);
    }
    
    /**
     * Update campaign metrics when an email is sent
     */
    @Transactional
    public void trackEmailSent(Campaign campaign, Integer count) {
        CampaignMetrics metrics = getCampaignMetrics(campaign);
        metrics.setTotalSent(metrics.getTotalSent() + count);
        campaignMetricsRepository.save(metrics);
    }
    
    /**
     * Update campaign metrics when an email is delivered
     */
    @Transactional
    public void trackEmailDelivered(Campaign campaign) {
        CampaignMetrics metrics = getCampaignMetrics(campaign);
        metrics.setTotalDelivered(metrics.getTotalDelivered() + 1);
        campaignMetricsRepository.save(metrics);
    }
    
    /**
     * Update campaign metrics when an email is opened
     */
    @Transactional
    public void trackEmailOpened(Campaign campaign, boolean isUniqueOpen) {
        CampaignMetrics metrics = getCampaignMetrics(campaign);
        metrics.setTotalOpened(metrics.getTotalOpened() + 1);
        if (isUniqueOpen) {
            metrics.setUniqueOpens(metrics.getUniqueOpens() + 1);
        }
        campaignMetricsRepository.save(metrics);
    }
    
    /**
     * Update campaign metrics when a link is clicked
     */
    @Transactional
    public void trackEmailClicked(Campaign campaign, boolean isUniqueClick) {
        CampaignMetrics metrics = getCampaignMetrics(campaign);
        metrics.setTotalClicked(metrics.getTotalClicked() + 1);
        if (isUniqueClick) {
            metrics.setUniqueClicks(metrics.getUniqueClicks() + 1);
        }
        campaignMetricsRepository.save(metrics);
    }
    
    /**
     * Update campaign metrics when an email bounces
     */
    @Transactional
    public void trackEmailBounced(Campaign campaign) {
        CampaignMetrics metrics = getCampaignMetrics(campaign);
        metrics.setTotalBounced(metrics.getTotalBounced() + 1);
        campaignMetricsRepository.save(metrics);
    }
    
    /**
     * Update campaign metrics when a recipient unsubscribes
     */
    @Transactional
    public void trackUnsubscribe(Campaign campaign) {
        CampaignMetrics metrics = getCampaignMetrics(campaign);
        metrics.setTotalUnsubscribed(metrics.getTotalUnsubscribed() + 1);
        campaignMetricsRepository.save(metrics);
    }
    
    /**
     * Update campaign metrics when a conversion happens
     */
    @Transactional
    public void trackConversion(Campaign campaign) {
        CampaignMetrics metrics = getCampaignMetrics(campaign);
        metrics.setTotalConversions(metrics.getTotalConversions() + 1);
        campaignMetricsRepository.save(metrics);
    }
    
    /**
     * Get campaign metrics, creating if necessary
     */
    private CampaignMetrics getCampaignMetrics(Campaign campaign) {
        return campaignMetricsRepository.findByCampaign(campaign)
            .orElseGet(() -> initializeCampaignMetrics(campaign));
    }
    
    /**
     * Get all campaign metrics for an organization
     */
    public List<CampaignMetrics> getAllCampaignMetrics(Organization organization) {
        return campaignMetricsRepository.findByOrganization(organization);
    }
    
    // ========== Segmentation Engine ==========
    
    /**
     * Apply segment rules to get filtered contacts
     */
    public List<Contact> getSegmentedContacts(Segment segment) {
        List<SegmentRule> rules = segmentRuleRepository.findBySegment(segment);
        
        // Get all contacts for the organization
        List<Contact> allContacts = contactRepository.findByOrganization(segment.getOrganization());
        
        // Apply filtering based on rules
        return allContacts.stream()
            .filter(contact -> matchesSegmentRules(contact, rules))
            .collect(Collectors.toList());
    }
    
    /**
     * Check if a contact matches all segment rules
     */
    private boolean matchesSegmentRules(Contact contact, List<SegmentRule> rules) {
        if (rules.isEmpty()) {
            return true; // No rules means all contacts match
        }
        
        boolean result = true;
        String lastLogicalOperator = "AND";
        
        for (SegmentRule rule : rules) {
            boolean ruleMatches = evaluateRule(contact, rule);
            
            if ("AND".equals(lastLogicalOperator)) {
                result = result && ruleMatches;
            } else if ("OR".equals(lastLogicalOperator)) {
                result = result || ruleMatches;
            }
            
            lastLogicalOperator = rule.getLogicalOperator();
        }
        
        return result;
    }
    
    /**
     * Evaluate a single segment rule against a contact
     */
    private boolean evaluateRule(Contact contact, SegmentRule rule) {
        String fieldValue = getContactFieldValue(contact, rule.getFieldName());
        String ruleValue = rule.getFieldValue();
        
        if (fieldValue == null) {
            return false;
        }
        
        switch (rule.getOperator()) {
            case "EQUALS":
                return fieldValue.equalsIgnoreCase(ruleValue);
            case "NOT_EQUALS":
                return !fieldValue.equalsIgnoreCase(ruleValue);
            case "CONTAINS":
                return fieldValue.toLowerCase().contains(ruleValue.toLowerCase());
            case "NOT_CONTAINS":
                return !fieldValue.toLowerCase().contains(ruleValue.toLowerCase());
            case "STARTS_WITH":
                return fieldValue.toLowerCase().startsWith(ruleValue.toLowerCase());
            case "ENDS_WITH":
                return fieldValue.toLowerCase().endsWith(ruleValue.toLowerCase());
            case "GREATER_THAN":
                try {
                    return Double.parseDouble(fieldValue) > Double.parseDouble(ruleValue);
                } catch (NumberFormatException e) {
                    return false;
                }
            case "LESS_THAN":
                try {
                    return Double.parseDouble(fieldValue) < Double.parseDouble(ruleValue);
                } catch (NumberFormatException e) {
                    return false;
                }
            case "IN":
                String[] values = ruleValue.split(",");
                for (String val : values) {
                    if (fieldValue.equalsIgnoreCase(val.trim())) {
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }
    
    /**
     * Get contact field value by field name
     */
    private String getContactFieldValue(Contact contact, String fieldName) {
        switch (fieldName.toLowerCase()) {
            case "email":
            case "contact_email":
                return contact.getContactEmail();
            case "name":
            case "contact_name":
                return contact.getContactName();
            case "phone":
                return contact.getPhone();
            default:
                return null;
        }
    }
    
    // ========== Drip Campaign Execution ==========
    
    /**
     * Execute a drip campaign step
     */
    @Async
    @Transactional
    public void executeDripCampaignStep(DripCampaignStep step, Contact contact) {
        CampaignTemplate template = step.getTemplate();
        
        switch (step.getActionType()) {
            case "SEND_EMAIL":
                sendCampaignEmail(template, contact);
                break;
            case "SEND_SMS":
                sendCampaignSMS(template, contact);
                break;
            case "CREATE_TASK":
                createFollowUpTask(contact);
                break;
            case "UPDATE_LEAD_SCORE":
                // Integrate with lead scoring if applicable
                break;
        }
    }
    
    /**
     * Send campaign email (placeholder for actual email service)
     */
    private void sendCampaignEmail(CampaignTemplate template, Contact contact) {
        // Replace variables in template content
        String contactName = contact.getContactName() != null ? contact.getContactName() : "";
        String[] nameParts = contactName.split(" ", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        
        String content = template.getContent()
            .replace("{firstName}", firstName)
            .replace("{lastName}", lastName)
            .replace("{name}", contactName)
            .replace("{email}", contact.getContactEmail() != null ? contact.getContactEmail() : "");
        
        // TODO: Integrate with actual email service
        System.out.println("Sending email to: " + contact.getContactEmail());
        System.out.println("Subject: " + template.getSubject());
        System.out.println("Content: " + content);
    }
    
    /**
     * Send campaign SMS (placeholder for actual SMS service)
     */
    private void sendCampaignSMS(CampaignTemplate template, Contact contact) {
        String contactName = contact.getContactName() != null ? contact.getContactName() : "";
        String[] nameParts = contactName.split(" ", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        
        String content = template.getContent()
            .replace("{firstName}", firstName)
            .replace("{name}", contactName)
            .replace("{phone}", contact.getPhone() != null ? contact.getPhone() : "");
        
        // TODO: Integrate with actual SMS service
        System.out.println("Sending SMS to: " + contact.getPhone());
        System.out.println("Content: " + content);
    }
    
    /**
     * Create follow-up task for contact
     */
    private void createFollowUpTask(Contact contact) {
        // Create an activity as a task
        Activity task = new Activity();
        task.setActivityType("TASK");
        task.setDescription("Follow up with " + contact.getContactName());
        task.setOrganization(contact.getOrganization());
        task.setContact(contact);
        // Save task (would need ActivityRepository autowired)
    }
    
    /**
     * Schedule drip campaign execution
     */
    @Async
    @Transactional
    public void scheduleDripCampaign(DripCampaign campaign, List<Contact> contacts) {
        List<DripCampaignStep> steps = dripCampaignStepRepository
            .findByDripCampaignOrderByStepOrderAsc(campaign);
        
        for (Contact contact : contacts) {
            for (DripCampaignStep step : steps) {
                if (!step.getIsActive()) {
                    continue;
                }
                
                // Calculate execution time
                OffsetDateTime executionTime = OffsetDateTime.now(ZoneOffset.UTC)
                    .plusDays(step.getDelayDays())
                    .plusHours(step.getDelayHours() != null ? step.getDelayHours() : 0);
                
                // TODO: Schedule the step for future execution
                // In a production system, this would use a job scheduler like Quartz
                System.out.println("Scheduling step " + step.getStepOrder() + 
                    " for contact " + contact.getContactEmail() + 
                    " at " + executionTime);
            }
        }
    }
    
    /**
     * Get active drip campaigns for an organization
     */
    public List<DripCampaign> getActiveDripCampaigns(Organization organization) {
        return dripCampaignRepository.findByOrganizationAndIsActive(organization, true);
    }
    
    /**
     * Get campaign performance summary
     */
    public CampaignMetrics getCampaignPerformance(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
            .orElseThrow(() -> new RuntimeException("Campaign not found"));
        return getCampaignMetrics(campaign);
    }
    
    // ========== Additional Automation Methods ==========
    
    /**
     * Trigger lead nurturing for a specific lead
     */
    @Async
    @Transactional
    public void triggerLeadNurturing(Lead lead) {
        // Send notification to lead owner
        notificationService.send(
            lead.getOrganization().getOrgId(),
            lead.getMember().getMemberId(),
            "LEAD_NURTURING",
            "Lead nurturing reminder",
            "Lead '" + lead.getLeadName() + "' needs nurturing"
        );
    }
}