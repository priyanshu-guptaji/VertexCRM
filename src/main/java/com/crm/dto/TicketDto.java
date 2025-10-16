package com.crm.dto;

import java.time.OffsetDateTime;

public class TicketDto {
    private Long ticketId;
    private String subject;
    private String description;
    private String status;
    private String priority;
    private Long orgId;
    private Long requesterId;
    private Long assigneeId;
    private Integer slaMinutes;
    private OffsetDateTime dueAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public TicketDto() {}

    public TicketDto(Long ticketId, String subject, String description, String status, String priority,
                     Long orgId, Long requesterId, Long assigneeId, Integer slaMinutes,
                     OffsetDateTime dueAt, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.ticketId = ticketId;
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.orgId = orgId;
        this.requesterId = requesterId;
        this.assigneeId = assigneeId;
        this.slaMinutes = slaMinutes;
        this.dueAt = dueAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Long getRequesterId() { return requesterId; }
    public void setRequesterId(Long requesterId) { this.requesterId = requesterId; }
    public Long getAssigneeId() { return assigneeId; }
    public void setAssigneeId(Long assigneeId) { this.assigneeId = assigneeId; }
    public Integer getSlaMinutes() { return slaMinutes; }
    public void setSlaMinutes(Integer slaMinutes) { this.slaMinutes = slaMinutes; }
    public OffsetDateTime getDueAt() { return dueAt; }
    public void setDueAt(OffsetDateTime dueAt) { this.dueAt = dueAt; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
