package com.crm.automation;

import com.crm.entity.Ticket;
import org.springframework.context.ApplicationEvent;

/**
 * Event triggered when a ticket status changes.
 */
public class TicketStatusChangeEvent extends ApplicationEvent {
    
    private final Ticket ticket;
    private final String oldStatus;
    private final String newStatus;
    
    public TicketStatusChangeEvent(Object source, Ticket ticket, String oldStatus, String newStatus) {
        super(source);
        this.ticket = ticket;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
    
    public Ticket getTicket() {
        return ticket;
    }
    
    public String getOldStatus() {
        return oldStatus;
    }
    
    public String getNewStatus() {
        return newStatus;
    }
}