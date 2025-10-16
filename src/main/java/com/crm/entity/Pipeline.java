package com.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Entity representing a sales pipeline configuration.
 * Defines stages and visualization settings for deal tracking.
 */
@Entity
@Table(name = "pipelines")
public class Pipeline {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pipeline_id")
    private Long pipelineId;
    
    @NotBlank(message = "Pipeline name is required")
    @Size(max = 100)
    @Column(name = "pipeline_name", nullable = false)
    private String pipelineName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "stages", columnDefinition = "TEXT", nullable = false)
    private String stages; // JSON array of stage names: ["Prospecting", "Qualification", "Proposal", "Negotiation", "Closed Won"]
    
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false; // Whether this is the default pipeline
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
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
    public Pipeline() {}
    
    public Pipeline(String pipelineName, String stages, Organization organization) {
        this.pipelineName = pipelineName;
        this.stages = stages;
        this.organization = organization;
    }
    
    // Getters and Setters
    public Long getPipelineId() {
        return pipelineId;
    }
    
    public void setPipelineId(Long pipelineId) {
        this.pipelineId = pipelineId;
    }
    
    public String getPipelineName() {
        return pipelineName;
    }
    
    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStages() {
        return stages;
    }
    
    public void setStages(String stages) {
        this.stages = stages;
    }
    
    public Boolean getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
