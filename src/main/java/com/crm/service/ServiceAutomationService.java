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
 * Service for handling Service Automation features including:
 * - Ticket Management
 * - Auto-Assignment
 * - SLA Tracking
 * - Customer Feedback
 */
@Service
public class ServiceAutomationService {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private TicketCategoryRepository ticketCategoryRepository;
    
    @Autowired
    private SlaPolicyRepository slaPolicyRepository;
    
    @Autowired
    private AutoAssignmentRuleRepository autoAssignmentRuleRepository;
    
    @Autowired
    private TicketFeedbackRepository ticketFeedbackRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private OrganizationRepository organizationRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    // ========== Ticket Auto-Assignment ==========
    
    /**
     * Auto-assign ticket based on rules
     */
    @Transactional
    public void autoAssignTicket(Ticket ticket) {
        // Get active assignment rules for the organization
        List<AutoAssignmentRule> rules = autoAssignmentRuleRepository
            .findByOrganizationAndIsActiveOrderByRulePriorityDesc(
                ticket.getOrganization(), true);
        
        // Apply rules in priority order
        for (AutoAssignmentRule rule : rules) {
            if (matchesAssignmentRule(ticket, rule)) {
                Member assignee = determineAssignee(rule, ticket.getOrganization());
                if (assignee != null) {
                    ticket.setAssignee(assignee);
                    ticketRepository.save(ticket);
                    
                    // Notify assignee
                    notificationService.send(
                        ticket.getOrganization().getOrgId(),
                        assignee.getMemberId(),
                        "TICKET_ASSIGNED",
                        "New ticket assigned",
                        "Ticket #" + ticket.getTicketId() + " has been assigned to you"
                    );
                    return;
                }
            }
        }
        
        // If no rules matched, assign to default category assignee
        if (ticket.getCategory() != null && ticket.getCategory().getDefaultAssignee() != null) {
            ticket.setAssignee(ticket.getCategory().getDefaultAssignee());
            ticketRepository.save(ticket);
        }
    }
    
    /**
     * Check if ticket matches assignment rule
     */
    private boolean matchesAssignmentRule(Ticket ticket, AutoAssignmentRule rule) {
        // Check category match
        if (rule.getCategory() != null && 
            (ticket.getCategory() == null || 
             !rule.getCategory().getCategoryId().equals(ticket.getCategory().getCategoryId()))) {
            return false;
        }
        
        // Check priority match
        if (rule.getPriority() != null && 
            (ticket.getPriority() == null || 
             !rule.getPriority().equals(ticket.getPriority()))) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Determine assignee based on assignment strategy
     */
    private Member determineAssignee(AutoAssignmentRule rule, Organization organization) {
        switch (rule.getAssignmentStrategy()) {
            case "SPECIFIC_AGENT":
                return rule.getSpecificAssignee();
                
            case "ROUND_ROBIN":
                return getRoundRobinAgent(organization, rule);
                
            case "LOAD_BALANCED":
                return getLeastBusyAgent(organization, rule);
                
            case "SKILL_BASED":
                // For skill-based, we'd need additional agent skills data
                return getRandomAvailableAgent(organization);
                
            default:
                return rule.getSpecificAssignee();
        }
    }
    
    /**
     * Get agent using round-robin strategy
     */
    private Member getRoundRobinAgent(Organization organization, AutoAssignmentRule rule) {
        // Get all members in the organization
        List<Member> members = memberRepository.findByOrganization(organization);
        if (members.isEmpty()) {
            return null;
        }
        
        // Simple round-robin - in a real system, you'd track the last assigned agent
        return members.get((int) (System.currentTimeMillis() % members.size()));
    }
    
    /**
     * Get least busy agent (fewest open tickets)
     */
    private Member getLeastBusyAgent(Organization organization, AutoAssignmentRule rule) {
        List<Member> members = memberRepository.findByOrganization(organization);
        if (members.isEmpty()) {
            return null;
        }
        
        return members.stream()
            .min((m1, m2) -> {
                long count1 = ticketRepository.countByAssigneeAndStatus(m1, "open");
                long count2 = ticketRepository.countByAssigneeAndStatus(m2, "open");
                return Long.compare(count1, count2);
            })
            .orElse(null);
    }
    
    /**
     * Get random available agent
     */
    private Member getRandomAvailableAgent(Organization organization) {
        List<Member> members = memberRepository.findByOrganization(organization);
        if (members.isEmpty()) {
            return null;
        }
        
        return members.get((int) (Math.random() * members.size()));
    }
    
    // ========== SLA Management ==========
    
    /**
     * Calculate and set SLA for a ticket
     */
    @Transactional
    public void calculateAndSetSLA(Ticket ticket) {
        // Get SLA policy based on category and priority
        SlaPolicy policy = getSlaPolicy(ticket);
        if (policy != null) {
            // Set first response SLA
            OffsetDateTime firstResponseDue = ticket.getCreatedAt()
                .plusMinutes(policy.getFirstResponseMinutes());
            ticket.setFirstResponseDue(firstResponseDue);
            
            // Set resolution SLA
            OffsetDateTime resolutionDue = ticket.getCreatedAt()
                .plusMinutes(policy.getResolutionMinutes());
            ticket.setResolutionDue(resolutionDue);
            
            ticketRepository.save(ticket);
        } else if (ticket.getCategory() != null && 
                   ticket.getCategory().getDefaultSlaMinutes() != null) {
            // Use category default SLA
            OffsetDateTime dueAt = ticket.getCreatedAt()
                .plusMinutes(ticket.getCategory().getDefaultSlaMinutes());
            ticket.setResolutionDue(dueAt);
            ticketRepository.save(ticket);
        }
    }
    
    /**
     * Get applicable SLA policy for a ticket
     */
    private SlaPolicy getSlaPolicy(Ticket ticket) {
        // Try to find specific policy for category and priority
        if (ticket.getCategory() != null && ticket.getPriority() != null) {
            Optional<SlaPolicy> policy = slaPolicyRepository
                .findByOrganizationAndCategoryAndPriority(
                    ticket.getOrganization(), 
                    ticket.getCategory(), 
                    ticket.getPriority());
            if (policy.isPresent()) {
                return policy.get();
            }
        }
        
        // Try to find policy for category only
        if (ticket.getCategory() != null) {
            List<SlaPolicy> policies = slaPolicyRepository
                .findActivePoliciesByCategory(ticket.getOrganization(), ticket.getCategory());
            if (!policies.isEmpty()) {
                return policies.get(0); // First one (highest priority)
            }
        }
        
        return null;
    }
    
    /**
     * Check for SLA breaches and send notifications
     */
    @Async
    @Transactional
    public void checkSLABreaches(Organization organization) {
        // Get all open tickets
        List<Ticket> openTickets = ticketRepository
            .findByOrganizationAndStatus(organization, "open");
        
        for (Ticket ticket : openTickets) {
            checkTicketSLABreaches(ticket);
        }
    }
    
    /**
     * Check specific ticket for SLA breaches
     */
    private void checkTicketSLABreaches(Ticket ticket) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        
        // Check first response SLA
        if (ticket.getFirstResponseDue() != null && 
            now.isAfter(ticket.getFirstResponseDue()) && 
            ticket.getFirstResponseAt() == null) {
            
            // First response SLA breached
            handleSLABreach(ticket, "FIRST_RESPONSE");
        }
        
        // Check resolution SLA
        if (ticket.getResolutionDue() != null && 
            now.isAfter(ticket.getResolutionDue()) && 
            !"closed".equals(ticket.getStatus())) {
            
            // Resolution SLA breached
            handleSLABreach(ticket, "RESOLUTION");
        }
    }
    
    /**
     * Handle SLA breach
     */
    public void handleSLABreach(Ticket ticket, String breachType) {
        // Update breach status
        if ("FIRST_RESPONSE".equals(breachType)) {
            ticket.setFirstResponseBreached(true);
        } else if ("RESOLUTION".equals(breachType)) {
            ticket.setResolutionBreached(true);
        }
        
        ticketRepository.save(ticket);
        
        // Notify assignee
        if (ticket.getAssignee() != null) {
            String message = "SLA " + breachType.toLowerCase().replace("_", " ") + 
                           " breached for ticket #" + ticket.getTicketId();
            
            notificationService.send(
                ticket.getOrganization().getOrgId(),
                ticket.getAssignee().getMemberId(),
                "SLA_BREACH",
                "SLA Breach Alert",
                message
            );
        }
        
        // Check if escalation is enabled
        SlaPolicy policy = getSlaPolicy(ticket);
        if (policy != null && policy.getEscalationEnabled() && 
            policy.getEscalationAssignee() != null) {
            
            // Escalate ticket
            ticket.setAssignee(policy.getEscalationAssignee());
            ticketRepository.save(ticket);
            
            // Notify new assignee
            notificationService.send(
                ticket.getOrganization().getOrgId(),
                policy.getEscalationAssignee().getMemberId(),
                "TICKET_ESCALATED",
                "Ticket Escalated",
                "Ticket #" + ticket.getTicketId() + " has been escalated to you due to SLA breach"
            );
        }
    }
    
    // ========== Ticket Feedback ==========
    
    /**
     * Create feedback for a closed ticket
     */
    @Transactional
    public TicketFeedback createFeedback(Ticket ticket, Integer rating, String comment, Contact respondent) {
        TicketFeedback feedback = new TicketFeedback();
        feedback.setTicket(ticket);
        feedback.setRating(rating);
        feedback.setComment(comment);
        feedback.setRespondent(respondent);
        feedback.setOrganization(ticket.getOrganization());
        
        return ticketFeedbackRepository.save(feedback);
    }
    
    /**
     * Get feedback for a ticket
     */
    public Optional<TicketFeedback> getFeedbackForTicket(Ticket ticket) {
        return ticketFeedbackRepository.findByTicket(ticket);
    }
    
    /**
     * Get average rating for an organization
     */
    public Double getAverageRating(Organization organization) {
        return ticketFeedbackRepository.getAverageRatingByOrganization(organization);
    }
    
    /**
     * Get feedback satisfaction metrics
     */
    public SatisfactionMetrics getFeedbackMetrics(Organization organization) {
        List<TicketFeedback> feedbacks = ticketFeedbackRepository.findByOrganization(organization);
        
        long total = feedbacks.size();
        long satisfied = feedbacks.stream().filter(TicketFeedback::getSatisfied).count();
        long unsatisfied = total - satisfied;
        
        double avgRating = feedbacks.stream()
            .mapToInt(TicketFeedback::getRating)
            .average()
            .orElse(0.0);
        
        return new SatisfactionMetrics(total, satisfied, unsatisfied, avgRating);
    }
    
    /**
     * Send feedback request for closed ticket
     */
    @Async
    public void sendFeedbackRequest(Ticket ticket) {
        if (ticket.getRequester() != null && ticket.getStatus().equals("closed")) {
            notificationService.send(
                ticket.getOrganization().getOrgId(),
                ticket.getRequester().getContactId(), // Assuming contact ID can be used
                "FEEDBACK_REQUEST",
                "How did we do?",
                "Please rate your experience with ticket #" + ticket.getTicketId()
            );
        }
    }
    
    // ========== Helper Classes ==========
    
    /**
     * Satisfaction metrics DTO
     */
    public static class SatisfactionMetrics {
        private long totalFeedback;
        private long satisfiedCount;
        private long unsatisfiedCount;
        private double averageRating;
        
        public SatisfactionMetrics(long total, long satisfied, long unsatisfied, double avgRating) {
            this.totalFeedback = total;
            this.satisfiedCount = satisfied;
            this.unsatisfiedCount = unsatisfied;
            this.averageRating = avgRating;
        }
        
        // Getters
        public long getTotalFeedback() { return totalFeedback; }
        public long getSatisfiedCount() { return satisfiedCount; }
        public long getUnsatisfiedCount() { return unsatisfiedCount; }
        public double getAverageRating() { return averageRating; }
        
        public double getSatisfactionRate() {
            return totalFeedback > 0 ? (satisfiedCount * 100.0) / totalFeedback : 0.0;
        }
    }
}