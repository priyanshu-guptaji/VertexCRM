package com.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Entity for defining ticket categories for service automation.
 * Helps organize and route support tickets efficiently.
 */
@Entity
@Table(name = "ticket_categories")
public class TicketCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;
    
    @NotBlank(message = "Category name is required")
    @Size(max = 100)
    @Column(name = "category_name", nullable = false)
    private String categoryName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "color_code")
    @Size(max = 7)
    private String colorCode; // Hex color for UI display (e.g., #FF5733)
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_assignee_id")
    private Member defaultAssignee; // Default agent to assign tickets in this category
    
    @Column(name = "default_priority")
    @Size(max = 20)
    private String defaultPriority = "medium"; // low, medium, high, urgent
    
    @Column(name = "default_sla_minutes")
    private Integer defaultSlaMinutes; // Default SLA time for this category
    
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
    public TicketCategory() {}
    
    public TicketCategory(String categoryName, Organization organization) {
        this.categoryName = categoryName;
        this.organization = organization;
    }
    
    // Getters and Setters
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getColorCode() {
        return colorCode;
    }
    
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Member getDefaultAssignee() {
        return defaultAssignee;
    }
    
    public void setDefaultAssignee(Member defaultAssignee) {
        this.defaultAssignee = defaultAssignee;
    }
    
    public String getDefaultPriority() {
        return defaultPriority;
    }
    
    public void setDefaultPriority(String defaultPriority) {
        this.defaultPriority = defaultPriority;
    }
    
    public Integer getDefaultSlaMinutes() {
        return defaultSlaMinutes;
    }
    
    public void setDefaultSlaMinutes(Integer defaultSlaMinutes) {
        this.defaultSlaMinutes = defaultSlaMinutes;
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
