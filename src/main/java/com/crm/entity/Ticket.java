package com.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticketId;

    @NotBlank
    @Size(max = 200)
    @Column(name = "subject", nullable = false)
    private String subject;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status; // open, in_progress, on_hold, resolved, closed

    @Column(name = "priority")
    private String priority; // low, medium, high, urgent

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private Contact requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private Member assignee;

    @Column(name = "sla_minutes")
    private Integer slaMinutes; // simple SLA target in minutes

    @Column(name = "due_at")
    private OffsetDateTime dueAt; // computed from created_at + sla

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private TicketCategory category;

    @Column(name = "first_response_due")
    private OffsetDateTime firstResponseDue;

    @Column(name = "first_response_at")
    private OffsetDateTime firstResponseAt;

    @Column(name = "resolution_due")
    private OffsetDateTime resolutionDue;

    @Column(name = "first_response_breached")
    private Boolean firstResponseBreached = false;

    @Column(name = "resolution_breached")
    private Boolean resolutionBreached = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
        if (slaMinutes != null && dueAt == null) {
            dueAt = createdAt.plusMinutes(slaMinutes);
        }
        if (status == null) status = "open";
        if (priority == null) priority = "medium";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    // Getters and Setters
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
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
    public Contact getRequester() { return requester; }
    public void setRequester(Contact requester) { this.requester = requester; }
    public Member getAssignee() { return assignee; }
    public void setAssignee(Member assignee) { this.assignee = assignee; }
    public Integer getSlaMinutes() { return slaMinutes; }
    public void setSlaMinutes(Integer slaMinutes) { this.slaMinutes = slaMinutes; }
    public OffsetDateTime getDueAt() { return dueAt; }
    public void setDueAt(OffsetDateTime dueAt) { this.dueAt = dueAt; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public TicketCategory getCategory() { return category; }
    public void setCategory(TicketCategory category) { this.category = category; }
    public OffsetDateTime getFirstResponseDue() { return firstResponseDue; }
    public void setFirstResponseDue(OffsetDateTime firstResponseDue) { this.firstResponseDue = firstResponseDue; }
    public OffsetDateTime getFirstResponseAt() { return firstResponseAt; }
    public void setFirstResponseAt(OffsetDateTime firstResponseAt) { this.firstResponseAt = firstResponseAt; }
    public OffsetDateTime getResolutionDue() { return resolutionDue; }
    public void setResolutionDue(OffsetDateTime resolutionDue) { this.resolutionDue = resolutionDue; }
    public Boolean getFirstResponseBreached() { return firstResponseBreached; }
    public void setFirstResponseBreached(Boolean firstResponseBreached) { this.firstResponseBreached = firstResponseBreached; }
    public Boolean getResolutionBreached() { return resolutionBreached; }
    public void setResolutionBreached(Boolean resolutionBreached) { this.resolutionBreached = resolutionBreached; }
}
