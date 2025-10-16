package com.crm.service;

import com.crm.entity.*;
import com.crm.repository.*;
import com.crm.dto.LeadScoreDto;
import com.crm.tenant.TenantContext;
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
 * Service for handling Sales Automation features including:
 * - Lead Scoring
 * - Deal Stage Automation
 * - Follow-up Reminders
 */
@Service
public class SalesAutomationService {
    
    @Autowired
    private LeadScoreRepository leadScoreRepository;
    
    @Autowired
    private LeadRepository leadRepository;
    
    @Autowired
    private DealStageRuleRepository dealStageRuleRepository;
    
    @Autowired
    private FollowUpRuleRepository followUpRuleRepository;
    
    @Autowired
    private DealRepository dealRepository;
    
    @Autowired
    private OrganizationRepository organizationRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private ActivityRepository activityRepository;
    
    // ========== Lead Scoring Methods ==========
    
    /**
     * Create or update lead score for a lead
     */
    @Transactional
    public LeadScore createOrUpdateLeadScore(Lead lead, Integer engagementScore, Integer demographicScore, Integer behaviorScore) {
        Optional<LeadScore> existingScore = leadScoreRepository.findByLead(lead);
        
        LeadScore leadScore;
        if (existingScore.isPresent()) {
            leadScore = existingScore.get();
        } else {
            leadScore = new LeadScore();
            leadScore.setLead(lead);
            leadScore.setOrganization(lead.getOrganization());
        }
        
        leadScore.setEngagementScore(engagementScore);
        leadScore.setDemographicScore(demographicScore);
        leadScore.setBehaviorScore(behaviorScore);
        leadScore.setLastActivityDate(OffsetDateTime.now(ZoneOffset.UTC));
        
        return leadScoreRepository.save(leadScore);
    }
    
    /**
     * Update lead score based on activity
     */
    @Transactional
    public void updateLeadScoreOnActivity(Lead lead, String activityType) {
        Optional<LeadScore> scoreOpt = leadScoreRepository.findByLead(lead);
        
        LeadScore leadScore;
        if (scoreOpt.isPresent()) {
            leadScore = scoreOpt.get();
        } else {
            leadScore = new LeadScore();
            leadScore.setLead(lead);
            leadScore.setOrganization(lead.getOrganization());
        }
        
        // Update metrics based on activity type
        switch (activityType) {
            case "EMAIL_OPEN":
                leadScore.setEmailOpens(leadScore.getEmailOpens() + 1);
                leadScore.setEngagementScore(leadScore.getEngagementScore() + 5);
                break;
            case "EMAIL_CLICK":
                leadScore.setEmailClicks(leadScore.getEmailClicks() + 1);
                leadScore.setEngagementScore(leadScore.getEngagementScore() + 10);
                break;
            case "WEBSITE_VISIT":
                leadScore.setWebsiteVisits(leadScore.getWebsiteVisits() + 1);
                leadScore.setBehaviorScore(leadScore.getBehaviorScore() + 3);
                break;
            case "FORM_SUBMIT":
                leadScore.setFormSubmissions(leadScore.getFormSubmissions() + 1);
                leadScore.setBehaviorScore(leadScore.getBehaviorScore() + 15);
                break;
        }
        
        leadScore.setLastActivityDate(OffsetDateTime.now(ZoneOffset.UTC));
        leadScoreRepository.save(leadScore);
        
        // Check if lead should be auto-converted
        checkAndAutoConvertLead(leadScore);
    }
    
    /**
     * Auto-convert high-scoring leads to opportunities
     */
    @Transactional
    public void checkAndAutoConvertLead(LeadScore leadScore) {
        // Auto-convert if total score >= 80 and not already converted
        if (leadScore.getTotalScore() >= 80 && !leadScore.getAutoConverted()) {
            Lead lead = leadScore.getLead();
            
            // Create a new Deal/Opportunity
            Deal deal = new Deal();
            deal.setDealName("Opportunity from " + lead.getLeadName());
            deal.setDealStage("Qualification");
            deal.setOrganization(lead.getOrganization());
            deal.setMember(lead.getMember());
            dealRepository.save(deal);
            
            // Mark as converted
            leadScore.setAutoConverted(true);
            leadScoreRepository.save(leadScore);
            
            // Send notification
            notificationService.send(
                lead.getOrganization().getOrgId(),
                lead.getMember().getMemberId(),
                "LEAD_CONVERTED",
                "High-scoring lead auto-converted",
                "Lead '" + lead.getLeadName() + "' with score " + leadScore.getTotalScore() + " has been automatically converted to an opportunity."
            );
        }
    }
    
    /**
     * Get all lead scores for an organization
     */
    public List<LeadScoreDto> getAllLeadScores(Organization organization) {
        List<LeadScore> scores = leadScoreRepository.findByOrganization(organization);
        return scores.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    /**
     * Get top-scoring leads
     */
    public List<LeadScoreDto> getTopLeads(Organization organization, Integer minScore) {
        List<LeadScore> topLeads = leadScoreRepository.findTopLeadsByScore(organization, minScore);
        return topLeads.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    // ========== Deal Stage Automation Methods ==========
    
    /**
     * Process deal stage automation based on trigger
     */
    @Transactional
    public void processDealStageAutomation(Deal deal, String triggerType) {
        Organization org = deal.getOrganization();
        String currentStage = deal.getDealStage();
        
        // Find matching rules
        List<DealStageRule> rules = dealStageRuleRepository
            .findByOrganizationAndSourceStageAndTriggerType(org, currentStage, triggerType);
        
        if (!rules.isEmpty()) {
            // Apply the highest priority rule
            DealStageRule rule = rules.stream()
                .filter(DealStageRule::getIsActive)
                .findFirst()
                .orElse(null);
            
            if (rule != null) {
                deal.setDealStage(rule.getTargetStage());
                dealRepository.save(deal);
                
                // Log activity
                Activity activity = new Activity();
                activity.setActivityType("DEAL_STAGE_CHANGED");
                activity.setDescription("Deal stage automatically changed from " + currentStage + " to " + rule.getTargetStage());
                activity.setOrganization(org);
                activity.setMember(deal.getMember());
                activityRepository.save(activity);
                
                // Notify assigned member
                notificationService.send(
                    org.getOrgId(),
                    deal.getMember().getMemberId(),
                    "DEAL_STAGE_CHANGED",
                    "Deal stage updated",
                    "Deal '" + deal.getDealName() + "' has been automatically moved to " + rule.getTargetStage()
                );
            }
        }
    }
    
    // ========== Follow-up Automation Methods ==========
    
    /**
     * Check and create follow-up tasks for stagnant entities
     */
    @Async
    @Transactional
    public void checkAndCreateFollowUps(Organization organization) {
        List<FollowUpRule> activeRules = followUpRuleRepository
            .findByOrganizationAndIsActive(organization, true);
        
        for (FollowUpRule rule : activeRules) {
            switch (rule.getEntityType()) {
                case "LEAD":
                    processLeadFollowUps(rule);
                    break;
                case "DEAL":
                    processDealFollowUps(rule);
                    break;
            }
        }
    }
    
    private void processLeadFollowUps(FollowUpRule rule) {
        Organization org = rule.getOrganization();
        OffsetDateTime cutoffDate = OffsetDateTime.now(ZoneOffset.UTC).minusDays(rule.getInactivityDays());
        
        // Find leads with no recent activity
        List<Lead> stagnantLeads = leadRepository.findByOrganization(org).stream()
            .filter(lead -> {
                Optional<LeadScore> scoreOpt = leadScoreRepository.findByLead(lead);
                return scoreOpt.isEmpty() || 
                       scoreOpt.get().getLastActivityDate() == null ||
                       scoreOpt.get().getLastActivityDate().isBefore(cutoffDate);
            })
            .collect(Collectors.toList());
        
        for (Lead lead : stagnantLeads) {
            if ("SEND_NOTIFICATION".equals(rule.getActionType())) {
                notificationService.send(
                    org.getOrgId(),
                    lead.getMember().getMemberId(),
                    "FOLLOW_UP_REMINDER",
                    "Follow-up reminder",
                    rule.getNotificationMessage() != null ? rule.getNotificationMessage() : 
                        "Lead '" + lead.getLeadName() + "' has been inactive for " + rule.getInactivityDays() + " days"
                );
            }
        }
    }
    
    private void processDealFollowUps(FollowUpRule rule) {
        Organization org = rule.getOrganization();
        OffsetDateTime cutoffDate = OffsetDateTime.now(ZoneOffset.UTC).minusDays(rule.getInactivityDays());
        
        // Find deals with no recent updates
        List<Deal> stagnantDeals = dealRepository.findByOrganization(org).stream()
            .filter(deal -> deal.getUpdatedAt().isBefore(cutoffDate))
            .collect(Collectors.toList());
        
        for (Deal deal : stagnantDeals) {
            if ("SEND_NOTIFICATION".equals(rule.getActionType())) {
                notificationService.send(
                    org.getOrgId(),
                    deal.getMember().getMemberId(),
                    "FOLLOW_UP_REMINDER",
                    "Follow-up reminder",
                    rule.getNotificationMessage() != null ? rule.getNotificationMessage() :
                        "Deal '" + deal.getDealName() + "' has not been updated for " + rule.getInactivityDays() + " days"
                );
            }
        }
    }
    
    // ========== Additional Automation Methods ==========
    
    /**
     * Trigger deal follow-up for a specific deal
     */
    @Async
    @Transactional
    public void triggerDealFollowUp(Deal deal) {
        // Send notification to deal owner
        notificationService.send(
            deal.getOrganization().getOrgId(),
            deal.getMember().getMemberId(),
            "DEAL_FOLLOW_UP",
            "Deal follow-up reminder",
            "Deal '" + deal.getDealName() + "' in '" + deal.getDealStage() + "' stage needs your attention"
        );
    }
    
    /**
     * Calculate lead score for a lead
     */
    @Transactional
    public void calculateLeadScore(Lead lead) {
        // This would typically involve complex calculations based on various factors
        // For now, we'll just trigger the existing update method
        updateLeadScoreOnActivity(lead, "daily_score_update");
    }
    
    // ========== Helper Methods ==========
    
    private LeadScoreDto convertToDto(LeadScore leadScore) {
        LeadScoreDto dto = new LeadScoreDto();
        dto.setScoreId(leadScore.getScoreId());
        dto.setLeadId(leadScore.getLead().getLeadId());
        dto.setLeadName(leadScore.getLead().getLeadName());
        dto.setLeadEmail(leadScore.getLead().getLeadEmail());
        dto.setEngagementScore(leadScore.getEngagementScore());
        dto.setDemographicScore(leadScore.getDemographicScore());
        dto.setBehaviorScore(leadScore.getBehaviorScore());
        dto.setTotalScore(leadScore.getTotalScore());
        dto.setGrade(leadScore.getGrade());
        dto.setEmailOpens(leadScore.getEmailOpens());
        dto.setEmailClicks(leadScore.getEmailClicks());
        dto.setWebsiteVisits(leadScore.getWebsiteVisits());
        dto.setFormSubmissions(leadScore.getFormSubmissions());
        dto.setLastActivityDate(leadScore.getLastActivityDate());
        dto.setAutoConverted(leadScore.getAutoConverted());
        dto.setOrgId(leadScore.getOrganization().getOrgId());
        dto.setCreatedAt(leadScore.getCreatedAt());
        dto.setUpdatedAt(leadScore.getUpdatedAt());
        return dto;
    }
}