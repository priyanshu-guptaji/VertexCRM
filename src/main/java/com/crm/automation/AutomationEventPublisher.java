package com.crm.automation;

import com.crm.entity.Lead;
import com.crm.entity.Deal;
import com.crm.entity.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publisher for automation events.
 * Triggers automation workflows based on business events.
 */
@Component
public class AutomationEventPublisher {
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    /**
     * Publish lead activity event
     */
    public void publishLeadActivityEvent(Lead lead, String activityType) {
        LeadActivityEvent event = new LeadActivityEvent(this, lead, activityType);
        eventPublisher.publishEvent(event);
    }
    
    /**
     * Publish deal stage change event
     */
    public void publishDealStageChangeEvent(Deal deal, String triggerType) {
        DealStageChangeEvent event = new DealStageChangeEvent(this, deal, triggerType);
        eventPublisher.publishEvent(event);
    }
    
    /**
     * Publish ticket created event
     */
    public void publishTicketCreatedEvent(Ticket ticket) {
        TicketCreatedEvent event = new TicketCreatedEvent(this, ticket);
        eventPublisher.publishEvent(event);
    }
    
    /**
     * Publish ticket status change event
     */
    public void publishTicketStatusChangeEvent(Ticket ticket, String oldStatus, String newStatus) {
        TicketStatusChangeEvent event = new TicketStatusChangeEvent(this, ticket, oldStatus, newStatus);
        eventPublisher.publishEvent(event);
    }
    
    /**
     * Publish ticket SLA breach event
     */
    public void publishTicketSlaBreachEvent(Ticket ticket, String breachType) {
        TicketSlaBreachEvent event = new TicketSlaBreachEvent(this, ticket, breachType);
        eventPublisher.publishEvent(event);
    }
}
