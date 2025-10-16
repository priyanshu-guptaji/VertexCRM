package com.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Entity for collecting customer feedback after ticket resolution.
 * Helps measure service quality and customer satisfaction.
 */
@Entity
@Table(name = "ticket_feedback")
public class TicketFeedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long feedbackId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false, unique = true)
    private Ticket ticket;
    
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    @Column(name = "rating", nullable = false)
    private Integer rating; // 1 to 5 stars
    
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment; // Optional customer feedback text
    
    @Column(name = "satisfied", nullable = false)
    private Boolean satisfied; // Quick yes/no satisfaction indicator
    
    @Column(name = "response_time_rating")
    @Min(value = 1)
    @Max(value = 5)
    private Integer responseTimeRating; // Rating for response speed
    
    @Column(name = "resolution_rating")
    @Min(value = 1)
    @Max(value = 5)
    private Integer resolutionRating; // Rating for solution quality
    
    @Column(name = "professionalism_rating")
    @Min(value = 1)
    @Max(value = 5)
    private Integer professionalismRating; // Rating for agent professionalism
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private Contact respondent; // Who provided the feedback
    
    @Column(name = "submitted_at", nullable = false)
    private OffsetDateTime submittedAt;
    
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
        if (submittedAt == null) {
            submittedAt = OffsetDateTime.now(ZoneOffset.UTC);
        }
        // Auto-set satisfied based on rating
        if (satisfied == null && rating != null) {
            satisfied = rating >= 4;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
    
    // Constructors
    public TicketFeedback() {}
    
    public TicketFeedback(Ticket ticket, Integer rating, Organization organization) {
        this.ticket = ticket;
        this.rating = rating;
        this.organization = organization;
    }
    
    // Getters and Setters
    public Long getFeedbackId() {
        return feedbackId;
    }
    
    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }
    
    public Ticket getTicket() {
        return ticket;
    }
    
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public Boolean getSatisfied() {
        return satisfied;
    }
    
    public void setSatisfied(Boolean satisfied) {
        this.satisfied = satisfied;
    }
    
    public Integer getResponseTimeRating() {
        return responseTimeRating;
    }
    
    public void setResponseTimeRating(Integer responseTimeRating) {
        this.responseTimeRating = responseTimeRating;
    }
    
    public Integer getResolutionRating() {
        return resolutionRating;
    }
    
    public void setResolutionRating(Integer resolutionRating) {
        this.resolutionRating = resolutionRating;
    }
    
    public Integer getProfessionalismRating() {
        return professionalismRating;
    }
    
    public void setProfessionalismRating(Integer professionalismRating) {
        this.professionalismRating = professionalismRating;
    }
    
    public Contact getRespondent() {
        return respondent;
    }
    
    public void setRespondent(Contact respondent) {
        this.respondent = respondent;
    }
    
    public OffsetDateTime getSubmittedAt() {
        return submittedAt;
    }
    
    public void setSubmittedAt(OffsetDateTime submittedAt) {
        this.submittedAt = submittedAt;
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
