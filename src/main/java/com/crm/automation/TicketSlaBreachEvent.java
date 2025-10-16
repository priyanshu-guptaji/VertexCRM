package com.crm.automation;

import com.crm.entity.Ticket;
import org.springframework.context.ApplicationEvent;

/**
 * Event triggered when a ticket SLA is breached.
 */
public class TicketSlaBreachEvent extends ApplicationEvent {
    
    private final Ticket ticket;
    private final String breachType; // "first_response" or "resolution"
    
    public TicketSlaBreachEvent(Object source, Ticket ticket, String breachType) {
        super(source);
        this.ticket = ticket;
        this.breachType = breachType;
    }
    
    public Ticket getTicket() {
        return ticket;
    }
    
    public String getBreachType() {
        return breachType;
    }
}