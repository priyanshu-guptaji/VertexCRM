package com.crm.automation;

import com.crm.entity.Lead;
import org.springframework.context.ApplicationEvent;

/**
 * Event triggered when lead activity occurs.
 */
public class LeadActivityEvent extends ApplicationEvent {
    
    private final Lead lead;
    private final String activityType;
    
    public LeadActivityEvent(Object source, Lead lead, String activityType) {
        super(source);
        this.lead = lead;
        this.activityType = activityType;
    }
    
    public Lead getLead() {
        return lead;
    }
    
    public String getActivityType() {
        return activityType;
    }
}