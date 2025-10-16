package com.crm.automation;

import com.crm.entity.Ticket;
import org.springframework.context.ApplicationEvent;

/**
 * Event triggered when a new ticket is created.
 */
public class TicketCreatedEvent extends ApplicationEvent {
    
    private final Ticket ticket;
    
    public TicketCreatedEvent(Object source, Ticket ticket) {
        super(source);
        this.ticket = ticket;
    }
    
    public Ticket getTicket() {
        return ticket;
    }
}