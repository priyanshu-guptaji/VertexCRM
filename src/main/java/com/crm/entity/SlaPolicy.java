package com.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Entity for defining SLA (Service Level Agreement) policies.
 * Sets response and resolution time targets for different ticket types.
 */
@Entity
@Table(name = "sla_policies")
public class SlaPolicy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sla_id")
    private Long slaId;
    
    @NotBlank(message = "SLA policy name is required")
    @Size(max = 100)
    @Column(name = "policy_name", nullable = false)
    private String policyName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private TicketCategory category; // Optional: specific category this SLA applies to
    
    @Column(name = "priority")
    @Size(max = 20)
    private String priority; // Optional: specific priority this SLA applies to
    
    @Column(name = "first_response_minutes", nullable = false)
    private Integer firstResponseMinutes; // Time to first response
    
    @Column(name = "resolution_minutes", nullable = false)
    private Integer resolutionMinutes; // Time to resolution
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "business_hours_only", nullable = false)
    private Boolean businessHoursOnly = false; // Whether to count only business hours
    
    @Column(name = "escalation_enabled", nullable = false)
    private Boolean escalationEnabled = false; // Auto-escalate when SLA breached
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escalation_assignee_id")
    private Member escalationAssignee; // Who to escalate to when SLA breached
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;
    
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
    public SlaPolicy() {}
    
    // Getters and Setters
    public Long getSlaId() {
        return slaId;
    }
    
    public void setSlaId(Long slaId) {
        this.slaId = slaId;
    }
    
    public String getPolicyName() {
        return policyName;
    }
    
    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public TicketCategory getCategory() {
        return category;
    }
    
    public void setCategory(TicketCategory category) {
        this.category = category;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public Integer getFirstResponseMinutes() {
        return firstResponseMinutes;
    }
    
    public void setFirstResponseMinutes(Integer firstResponseMinutes) {
        this.firstResponseMinutes = firstResponseMinutes;
    }
    
    public Integer getResolutionMinutes() {
        return resolutionMinutes;
    }
    
    public void setResolutionMinutes(Integer resolutionMinutes) {
        this.resolutionMinutes = resolutionMinutes;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Boolean getBusinessHoursOnly() {
        return businessHoursOnly;
    }
    
    public void setBusinessHoursOnly(Boolean businessHoursOnly) {
        this.businessHoursOnly = businessHoursOnly;
    }
    
    public Boolean getEscalationEnabled() {
        return escalationEnabled;
    }
    
    public void setEscalationEnabled(Boolean escalationEnabled) {
        this.escalationEnabled = escalationEnabled;
    }
    
    public Member getEscalationAssignee() {
        return escalationAssignee;
    }
    
    public void setEscalationAssignee(Member escalationAssignee) {
        this.escalationAssignee = escalationAssignee;
    }
    
    public Organization getOrganization() {
        return organization;
    }
    
    public void setOrganization(Organization organization) {
        this.organization = organization;
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
