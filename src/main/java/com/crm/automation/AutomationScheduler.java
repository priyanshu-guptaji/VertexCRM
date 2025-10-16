package com.crm.automation;

import com.crm.entity.Lead;
import com.crm.entity.Deal;
import com.crm.entity.Ticket;
import com.crm.repository.LeadRepository;
import com.crm.repository.DealRepository;
import com.crm.repository.TicketRepository;
import com.crm.service.SalesAutomationService;
import com.crm.service.MarketingAutomationService;
import com.crm.service.ServiceAutomationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Scheduler for time-based automation triggers.
 * Handles scheduled tasks for lead nurturing, deal follow-ups, and SLA monitoring.
 */
@Component
public class AutomationScheduler {
    
    @Autowired
    private LeadRepository leadRepository;
    
    @Autowired
    private DealRepository dealRepository;
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private AutomationEventPublisher eventPublisher;
    
    @Autowired
    private SalesAutomationService salesAutomationService;
    
    @Autowired
    private MarketingAutomationService marketingAutomationService;
    
    @Autowired
    private ServiceAutomationService serviceAutomationService;
    
    /**
     * Check for leads that need nurturing every hour
     */
    @Scheduled(fixedRate = 3600000) // Every hour
    public void checkLeadNurturing() {
        try {
            // Find leads that haven't been contacted in 3 days
            OffsetDateTime threeDaysAgo = OffsetDateTime.now().minusDays(3);
            List<Lead> inactiveLeads = leadRepository.findLeadsWithNoRecentActivity(threeDaysAgo);
            
            for (Lead lead : inactiveLeads) {
                // Publish lead activity event for automation
                eventPublisher.publishLeadActivityEvent(lead, "inactivity_detected");
                
                // Trigger lead nurturing workflow
                marketingAutomationService.triggerLeadNurturing(lead);
            }
        } catch (Exception e) {
            System.err.println("Error in lead nurturing scheduler: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Check for deals that need follow-up every 2 hours
     */
    @Scheduled(fixedRate = 7200000) // Every 2 hours
    public void checkDealFollowUps() {
        try {
            // Find deals in "Negotiation" stage that haven't been updated in 2 days
            OffsetDateTime twoDaysAgo = OffsetDateTime.now().minusDays(2);
            List<Deal> staleDeals = dealRepository.findStaleDeals("Negotiation", twoDaysAgo);
            
            for (Deal deal : staleDeals) {
                // Publish deal stage change event for automation
                eventPublisher.publishDealStageChangeEvent(deal, "stale_deal_followup");
                
                // Trigger deal follow-up workflow
                salesAutomationService.triggerDealFollowUp(deal);
            }
        } catch (Exception e) {
            System.err.println("Error in deal follow-up scheduler: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Monitor SLA breaches every 30 minutes
     */
    @Scheduled(fixedRate = 1800000) // Every 30 minutes
    public void monitorSLABreaches() {
        try {
            // Find tickets that are approaching or have breached SLA
            List<Ticket> tickets = ticketRepository.findTicketsNeedingSLAMonitoring();
            
            for (Ticket ticket : tickets) {
                // Check first response SLA
                if (ticket.getFirstResponseDue() != null && 
                    OffsetDateTime.now().isAfter(ticket.getFirstResponseDue()) &&
                    ticket.getFirstResponseAt() == null) {
                    
                    if (!ticket.getFirstResponseBreached()) {
                        // Mark first response as breached
                        ticket.setFirstResponseBreached(true);
                        ticketRepository.save(ticket);
                        
                        // Publish SLA breach event
                        eventPublisher.publishTicketSlaBreachEvent(ticket, "first_response");
                        
                        // Trigger SLA breach workflow
                        serviceAutomationService.handleSLABreach(ticket, "FIRST_RESPONSE");
                    }
                }
                
                // Check resolution SLA
                if (ticket.getResolutionDue() != null && 
                    OffsetDateTime.now().isAfter(ticket.getResolutionDue()) &&
                    !"resolved".equalsIgnoreCase(ticket.getStatus()) &&
                    !"closed".equalsIgnoreCase(ticket.getStatus())) {
                    
                    if (!ticket.getResolutionBreached()) {
                        // Mark resolution as breached
                        ticket.setResolutionBreached(true);
                        ticketRepository.save(ticket);
                        
                        // Publish SLA breach event
                        eventPublisher.publishTicketSlaBreachEvent(ticket, "resolution");
                        
                        // Trigger SLA breach workflow
                        serviceAutomationService.handleSLABreach(ticket, "RESOLUTION");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in SLA monitoring scheduler: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Daily lead scoring updates at 2 AM
     */
    @Scheduled(cron = "0 0 2 * * ?") // Daily at 2 AM
    public void updateLeadScores() {
        try {
            // Get all leads for scoring update
            List<Lead> leads = leadRepository.findAll();
            
            for (Lead lead : leads) {
                // Recalculate lead score
                salesAutomationService.calculateLeadScore(lead);
                
                // Publish lead activity event for automation
                eventPublisher.publishLeadActivityEvent(lead, "daily_score_update");
            }
        } catch (Exception e) {
            System.err.println("Error in lead scoring scheduler: " + e.getMessage());
            e.printStackTrace();
        }
    }
}