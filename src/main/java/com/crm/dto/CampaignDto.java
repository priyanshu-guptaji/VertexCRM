package com.crm.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class CampaignDto {
    private Long campaignId;
    private String name;
    private String type;
    private String status;
    private OffsetDateTime startAt;
    private OffsetDateTime endAt;
    private BigDecimal budget;
    private BigDecimal cost;
    private BigDecimal revenue;
    private Long orgId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public CampaignDto() {}

    public CampaignDto(Long campaignId, String name, String type, String status, OffsetDateTime startAt,
                       OffsetDateTime endAt, BigDecimal budget, BigDecimal cost, BigDecimal revenue,
                       Long orgId, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.campaignId = campaignId;
        this.name = name;
        this.type = type;
        this.status = status;
        this.startAt = startAt;
        this.endAt = endAt;
        this.budget = budget;
        this.cost = cost;
        this.revenue = revenue;
        this.orgId = orgId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public OffsetDateTime getStartAt() { return startAt; }
    public void setStartAt(OffsetDateTime startAt) { this.startAt = startAt; }
    public OffsetDateTime getEndAt() { return endAt; }
    public void setEndAt(OffsetDateTime endAt) { this.endAt = endAt; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
    public BigDecimal getRevenue() { return revenue; }
    public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
