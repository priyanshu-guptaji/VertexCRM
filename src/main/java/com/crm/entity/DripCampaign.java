package com.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Entity for managing automated drip campaigns.
 * Sends a series of timed messages based on user actions or schedules.
 */
@Entity
@Table(name = "drip_campaigns")
public class DripCampaign {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drip_campaign_id")
    private Long dripCampaignId;
    
    @NotBlank(message = "Campaign name is required")
    @Size(max = 100)
    @Column(name = "campaign_name", nullable = false)
    private String campaignName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @NotBlank(message = "Trigger type is required")
    @Size(max = 50)
    @Column(name = "trigger_type", nullable = false)
    private String triggerType; // LEAD_CREATED, DEAL_STAGE_CHANGED, FORM_SUBMITTED, INACTIVITY
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment_id")
    private Segment targetSegment; // Optional: target specific segment
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Member createdBy;
    
    @OneToMany(mappedBy = "dripCampaign", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DripCampaignStep> steps;
    
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
    public DripCampaign() {}
    
    // Getters and Setters
    public Long getDripCampaignId() {
        return dripCampaignId;
    }
    
    public void setDripCampaignId(Long dripCampaignId) {
        this.dripCampaignId = dripCampaignId;
    }
    
    public String getCampaignName() {
        return campaignName;
    }
    
    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    public Segment getTargetSegment() {
        return targetSegment;
    }
    
    public void setTargetSegment(Segment targetSegment) {
        this.targetSegment = targetSegment;
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
    
    public List<DripCampaignStep> getSteps() {
        return steps;
    }
    
    public void setSteps(List<DripCampaignStep> steps) {
        this.steps = steps;
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
