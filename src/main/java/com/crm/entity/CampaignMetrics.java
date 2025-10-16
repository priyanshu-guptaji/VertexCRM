package com.crm.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Entity for tracking detailed campaign performance metrics.
 * Stores analytics data for campaign effectiveness analysis.
 */
@Entity
@Table(name = "campaign_metrics")
public class CampaignMetrics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metrics_id")
    private Long metricsId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false, unique = true)
    private Campaign campaign;
    
    @Column(name = "total_sent", nullable = false)
    private Integer totalSent = 0; // Total messages/emails sent
    
    @Column(name = "total_delivered", nullable = false)
    private Integer totalDelivered = 0; // Successfully delivered
    
    @Column(name = "total_opened", nullable = false)
    private Integer totalOpened = 0; // Email opens
    
    @Column(name = "total_clicked", nullable = false)
    private Integer totalClicked = 0; // Link clicks
    
    @Column(name = "total_bounced", nullable = false)
    private Integer totalBounced = 0; // Delivery failures
    
    @Column(name = "total_unsubscribed", nullable = false)
    private Integer totalUnsubscribed = 0; // Unsubscribe requests
    
    @Column(name = "total_conversions", nullable = false)
    private Integer totalConversions = 0; // Successful conversions (e.g., form submissions, purchases)
    
    @Column(name = "total_replies", nullable = false)
    private Integer totalReplies = 0; // Email replies
    
    @Column(name = "unique_opens", nullable = false)
    private Integer uniqueOpens = 0; // Unique recipients who opened
    
    @Column(name = "unique_clicks", nullable = false)
    private Integer uniqueClicks = 0; // Unique recipients who clicked
    
    @Column(name = "open_rate")
    private Double openRate; // Percentage
    
    @Column(name = "click_rate")
    private Double clickRate; // Percentage
    
    @Column(name = "conversion_rate")
    private Double conversionRate; // Percentage
    
    @Column(name = "bounce_rate")
    private Double bounceRate; // Percentage
    
    @Column(name = "unsubscribe_rate")
    private Double unsubscribeRate; // Percentage
    
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
        calculateRates();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
        calculateRates();
    }
    
    // Calculate all percentage rates
    private void calculateRates() {
        if (totalSent > 0) {
            this.openRate = (totalOpened * 100.0) / totalSent;
            this.clickRate = (totalClicked * 100.0) / totalSent;
            this.conversionRate = (totalConversions * 100.0) / totalSent;
            this.bounceRate = (totalBounced * 100.0) / totalSent;
            this.unsubscribeRate = (totalUnsubscribed * 100.0) / totalSent;
        }
    }
    
    // Getters and Setters
    public Long getMetricsId() {
        return metricsId;
    }
    
    public void setMetricsId(Long metricsId) {
        this.metricsId = metricsId;
    }
    
    public Campaign getCampaign() {
        return campaign;
    }
    
    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }
    
    public Integer getTotalSent() {
        return totalSent;
    }
    
    public void setTotalSent(Integer totalSent) {
        this.totalSent = totalSent;
    }
    
    public Integer getTotalDelivered() {
        return totalDelivered;
    }
    
    public void setTotalDelivered(Integer totalDelivered) {
        this.totalDelivered = totalDelivered;
    }
    
    public Integer getTotalOpened() {
        return totalOpened;
    }
    
    public void setTotalOpened(Integer totalOpened) {
        this.totalOpened = totalOpened;
    }
    
    public Integer getTotalClicked() {
        return totalClicked;
    }
    
    public void setTotalClicked(Integer totalClicked) {
        this.totalClicked = totalClicked;
    }
    
    public Integer getTotalBounced() {
        return totalBounced;
    }
    
    public void setTotalBounced(Integer totalBounced) {
        this.totalBounced = totalBounced;
    }
    
    public Integer getTotalUnsubscribed() {
        return totalUnsubscribed;
    }
    
    public void setTotalUnsubscribed(Integer totalUnsubscribed) {
        this.totalUnsubscribed = totalUnsubscribed;
    }
    
    public Integer getTotalConversions() {
        return totalConversions;
    }
    
    public void setTotalConversions(Integer totalConversions) {
        this.totalConversions = totalConversions;
    }
    
    public Integer getTotalReplies() {
        return totalReplies;
    }
    
    public void setTotalReplies(Integer totalReplies) {
        this.totalReplies = totalReplies;
    }
    
    public Integer getUniqueOpens() {
        return uniqueOpens;
    }
    
    public void setUniqueOpens(Integer uniqueOpens) {
        this.uniqueOpens = uniqueOpens;
    }
    
    public Integer getUniqueClicks() {
        return uniqueClicks;
    }
    
    public void setUniqueClicks(Integer uniqueClicks) {
        this.uniqueClicks = uniqueClicks;
    }
    
    public Double getOpenRate() {
        return openRate;
    }
    
    public void setOpenRate(Double openRate) {
        this.openRate = openRate;
    }
    
    public Double getClickRate() {
        return clickRate;
    }
    
    public void setClickRate(Double clickRate) {
        this.clickRate = clickRate;
    }
    
    public Double getConversionRate() {
        return conversionRate;
    }
    
    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }
    
    public Double getBounceRate() {
        return bounceRate;
    }
    
    public void setBounceRate(Double bounceRate) {
        this.bounceRate = bounceRate;
    }
    
    public Double getUnsubscribeRate() {
        return unsubscribeRate;
    }
    
    public void setUnsubscribeRate(Double unsubscribeRate) {
        this.unsubscribeRate = unsubscribeRate;
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
