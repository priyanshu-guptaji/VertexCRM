package com.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Entity representing individual steps in a drip campaign.
 * Each step has a delay and action to execute.
 */
@Entity
@Table(name = "drip_campaign_steps")
public class DripCampaignStep {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "step_id")
    private Long stepId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drip_campaign_id", nullable = false)
    private DripCampaign dripCampaign;
    
    @Column(name = "step_order", nullable = false)
    private Integer stepOrder; // Sequence order of this step
    
    @Column(name = "delay_days", nullable = false)
    private Integer delayDays; // Days to wait after previous step (0 for immediate)
    
    @Column(name = "delay_hours")
    private Integer delayHours = 0; // Additional hours to wait
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private CampaignTemplate template; // Template to use for this step
    
    @NotBlank(message = "Action type is required")
    @Size(max = 50)
    @Column(name = "action_type", nullable = false)
    private String actionType; // SEND_EMAIL, SEND_SMS, CREATE_TASK, UPDATE_LEAD_SCORE
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
    
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
    
    // Constructors
    public DripCampaignStep() {}
    
    // Getters and Setters
    public Long getStepId() {
        return stepId;
    }
    
    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }
    
    public DripCampaign getDripCampaign() {
        return dripCampaign;
    }
    
    public void setDripCampaign(DripCampaign dripCampaign) {
        this.dripCampaign = dripCampaign;
    }
    
    public Integer getStepOrder() {
        return stepOrder;
    }
    
    public void setStepOrder(Integer stepOrder) {
        this.stepOrder = stepOrder;
    }
    
    public Integer getDelayDays() {
        return delayDays;
    }
    
    public void setDelayDays(Integer delayDays) {
        this.delayDays = delayDays;
    }
    
    public Integer getDelayHours() {
        return delayHours;
    }
    
    public void setDelayHours(Integer delayHours) {
        this.delayHours = delayHours;
    }
    
    public CampaignTemplate getTemplate() {
        return template;
    }
    
    public void setTemplate(CampaignTemplate template) {
        this.template = template;
    }
    
    public String getActionType() {
        return actionType;
    }
    
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
