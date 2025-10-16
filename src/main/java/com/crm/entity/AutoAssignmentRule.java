package com.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Entity for defining automatic ticket assignment rules.
 * Assigns tickets to agents based on priority, category, or workload.
 */
@Entity
@Table(name = "auto_assignment_rules")
public class AutoAssignmentRule {
    
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private TicketCategory category; // Assign based on category
    
    @Column(name = "priority")
    @Size(max = 20)
    private String priority; // Assign based on priority
    
    @NotBlank(message = "Assignment strategy is required")
    @Size(max = 50)
    @Column(name = "assignment_strategy", nullable = false)
    private String assignmentStrategy; // ROUND_ROBIN, LOAD_BALANCED, SKILL_BASED, SPECIFIC_AGENT
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specific_assignee_id")
    private Member specificAssignee; // For SPECIFIC_AGENT strategy
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "rule_priority")
    private Integer rulePriority = 0; // Higher priority rules execute first
    
    @Column(name = "max_tickets_per_agent")
    private Integer maxTicketsPerAgent; // For load balancing
    
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
    public AutoAssignmentRule() {}
    
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
    
    public String getAssignmentStrategy() {
        return assignmentStrategy;
    }
    
    public void setAssignmentStrategy(String assignmentStrategy) {
        this.assignmentStrategy = assignmentStrategy;
    }
    
    public Member getSpecificAssignee() {
        return specificAssignee;
    }
    
    public void setSpecificAssignee(Member specificAssignee) {
        this.specificAssignee = specificAssignee;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Integer getRulePriority() {
        return rulePriority;
    }
    
    public void setRulePriority(Integer rulePriority) {
        this.rulePriority = rulePriority;
    }
    
    public Integer getMaxTicketsPerAgent() {
        return maxTicketsPerAgent;
    }
    
    public void setMaxTicketsPerAgent(Integer maxTicketsPerAgent) {
        this.maxTicketsPerAgent = maxTicketsPerAgent;
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
