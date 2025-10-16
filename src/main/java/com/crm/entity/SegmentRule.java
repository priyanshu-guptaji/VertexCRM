package com.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Entity for defining contact segmentation rules for targeted marketing.
 * Allows filtering contacts based on various criteria.
 */
@Entity
@Table(name = "segment_rules")
public class SegmentRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "segment_rule_id")
    private Long segmentRuleId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment_id", nullable = false)
    private Segment segment;
    
    @NotBlank(message = "Field name is required")
    @Size(max = 100)
    @Column(name = "field_name", nullable = false)
    private String fieldName; // e.g., "location", "leadScore", "industry"
    
    @NotBlank(message = "Operator is required")
    @Size(max = 50)
    @Column(name = "operator", nullable = false)
    private String operator; // EQUALS, CONTAINS, GREATER_THAN, LESS_THAN, IN, NOT_IN
    
    @Column(name = "field_value", columnDefinition = "TEXT", nullable = false)
    private String fieldValue; // The value to compare against
    
    @Column(name = "logical_operator")
    @Size(max = 10)
    private String logicalOperator = "AND"; // AND, OR for combining with other rules
    
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
    public SegmentRule() {}
    
    // Getters and Setters
    public Long getSegmentRuleId() {
        return segmentRuleId;
    }
    
    public void setSegmentRuleId(Long segmentRuleId) {
        this.segmentRuleId = segmentRuleId;
    }
    
    public Segment getSegment() {
        return segment;
    }
    
    public void setSegment(Segment segment) {
        this.segment = segment;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public void setOperator(String operator) {
        this.operator = operator;
    }
    
    public String getFieldValue() {
        return fieldValue;
    }
    
    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
    
    public String getLogicalOperator() {
        return logicalOperator;
    }
    
    public void setLogicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
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
