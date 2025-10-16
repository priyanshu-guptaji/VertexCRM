package com.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Entity for defining automated deal stage progression rules.
 * Triggers automatic stage updates based on specific conditions.
 */
@Entity
@Table(name = "deal_stage_rules")
public class DealStageRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Long ruleId;
    
    @NotBlank(message = "Rule name is required")
    @Size(max = 100)
    @Column(name = "rule_name", nullable = false)
    private String ruleName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @NotBlank(message = "Source stage is required")
    @Size(max = 50)
    @Column(name = "source_stage", nullable = false)
    private String sourceStage; // Current stage that triggers the rule
    
    @NotBlank(message = "Target stage is required")
    @Size(max = 50)
    @Column(name = "target_stage", nullable = false)
    private String targetStage; // Stage to move to when rule matches
    
    @NotBlank(message = "Trigger type is required")
    @Size(max = 50)
    @Column(name = "trigger_type", nullable = false)
    private String triggerType; // EMAIL_OPENED, QUOTE_SENT, MEETING_SCHEDULED, PROPOSAL_SENT, etc.
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "priority")
    private Integer priority = 0; // Higher priority rules execute first
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Member createdBy;
    
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
    public DealStageRule() {}
    
    // Getters and Setters
    public Long getRuleId() {
        return ruleId;
    }
    
    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }
    
    public String getRuleName() {
        return ruleName;
    }
    
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSourceStage() {
        return sourceStage;
    }
    
    public void setSourceStage(String sourceStage) {
        this.sourceStage = sourceStage;
    }
    
    public String getTargetStage() {
        return targetStage;
    }
    
    public void setTargetStage(String targetStage) {
        this.targetStage = targetStage;
    }
    
    public String getTriggerType() {
        return triggerType;
    }
    
    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Integer getPriority() {
        return priority;
    }
    
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    
    public Organization getOrganization() {
        return organization;
    }
    
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
    
    public Member getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Member createdBy) {
        this.createdBy = createdBy;
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
