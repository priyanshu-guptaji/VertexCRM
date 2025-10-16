package com.crm.dto;

import java.time.OffsetDateTime;

public class SegmentDto {
    private Long segmentId;
    private Long orgId;
    private String name;
    private String definitionJson;
    private OffsetDateTime createdAt;

    public SegmentDto() {}

    public SegmentDto(Long segmentId, Long orgId, String name, String definitionJson, OffsetDateTime createdAt) {
        this.segmentId = segmentId;
        this.orgId = orgId;
        this.name = name;
        this.definitionJson = definitionJson;
        this.createdAt = createdAt;
    }

    public Long getSegmentId() { return segmentId; }
    public void setSegmentId(Long segmentId) { this.segmentId = segmentId; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDefinitionJson() { return definitionJson; }
    public void setDefinitionJson(String definitionJson) { this.definitionJson = definitionJson; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
