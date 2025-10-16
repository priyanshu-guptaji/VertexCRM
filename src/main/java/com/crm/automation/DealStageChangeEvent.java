package com.crm.automation;

import com.crm.entity.Deal;
import org.springframework.context.ApplicationEvent;

/**
 * Event triggered when a deal stage changes.
 */
public class DealStageChangeEvent extends ApplicationEvent {
    
    private final Deal deal;
    private final String triggerType;
    
    public DealStageChangeEvent(Object source, Deal deal, String triggerType) {
        super(source);
        this.deal = deal;
        this.triggerType = triggerType;
    }
    
    public Deal getDeal() {
        return deal;
    }
    
    public String getTriggerType() {
        return triggerType;
    }
}