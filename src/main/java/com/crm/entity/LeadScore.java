package com.crm.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Entity for tracking lead scoring metrics and automated quality assessment.
 * Helps identify high-quality leads based on engagement and activity.
 */
@Entity
@Table(name = "lead_scores")
public class LeadScore {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long scoreId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false, unique = true)
    private Lead lead;
    
    @Column(name = "engagement_score", nullable = false)
    private Integer engagementScore = 0; // Based on email opens, clicks, website visits
    
    @Column(name = "demographic_score", nullable = false)
    private Integer demographicScore = 0; // Based on company size, industry, location
    
    @Column(name = "behavior_score", nullable = false)
    private Integer behaviorScore = 0; // Based on content downloads, webinar attendance
    
    @Column(name = "total_score", nullable = false)
    private Integer totalScore = 0; // Sum of all scores
    
    @Column(name = "grade")
    private String grade; // A, B, C, D based on total score
    
    @Column(name = "email_opens", nullable = false)
    private Integer emailOpens = 0;
    
    @Column(name = "email_clicks", nullable = false)
    private Integer emailClicks = 0;
    
    @Column(name = "website_visits", nullable = false)
    private Integer websiteVisits = 0;
    
    @Column(name = "form_submissions", nullable = false)
    private Integer formSubmissions = 0;
    
    @Column(name = "last_activity_date")
    private OffsetDateTime lastActivityDate;
    
    @Column(name = "auto_converted", nullable = false)
    private Boolean autoConverted = false; // Whether lead was auto-converted to opportunity
    
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
        calculateTotalScore();
        assignGrade();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
        calculateTotalScore();
        assignGrade();
    }
    
    // Calculate total score from individual components
    private void calculateTotalScore() {
        this.totalScore = this.engagementScore + this.demographicScore + this.behaviorScore;
    }
    
    // Assign grade based on total score
    private void assignGrade() {
        if (totalScore >= 80) {
            this.grade = "A";
        } else if (totalScore >= 60) {
            this.grade = "B";
        } else if (totalScore >= 40) {
            this.grade = "C";
        } else {
            this.grade = "D";
        }
    }
    
    // Getters and Setters
    public Long getScoreId() {
        return scoreId;
    }
    
    public void setScoreId(Long scoreId) {
        this.scoreId = scoreId;
    }
    
    public Lead getLead() {
        return lead;
    }
    
    public void setLead(Lead lead) {
        this.lead = lead;
    }
    
    public Integer getEngagementScore() {
        return engagementScore;
    }
    
    public void setEngagementScore(Integer engagementScore) {
        this.engagementScore = engagementScore;
    }
    
    public Integer getDemographicScore() {
        return demographicScore;
    }
    
    public void setDemographicScore(Integer demographicScore) {
        this.demographicScore = demographicScore;
    }
    
    public Integer getBehaviorScore() {
        return behaviorScore;
    }
    
    public void setBehaviorScore(Integer behaviorScore) {
        this.behaviorScore = behaviorScore;
    }
    
    public Integer getTotalScore() {
        return totalScore;
    }
    
    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }
    
    public String getGrade() {
        return grade;
    }
    
    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    public Integer getEmailOpens() {
        return emailOpens;
    }
    
    public void setEmailOpens(Integer emailOpens) {
        this.emailOpens = emailOpens;
    }
    
    public Integer getEmailClicks() {
        return emailClicks;
    }
    
    public void setEmailClicks(Integer emailClicks) {
        this.emailClicks = emailClicks;
    }
    
    public Integer getWebsiteVisits() {
        return websiteVisits;
    }
    
    public void setWebsiteVisits(Integer websiteVisits) {
        this.websiteVisits = websiteVisits;
    }
    
    public Integer getFormSubmissions() {
        return formSubmissions;
    }
    
    public void setFormSubmissions(Integer formSubmissions) {
        this.formSubmissions = formSubmissions;
    }
    
    public OffsetDateTime getLastActivityDate() {
        return lastActivityDate;
    }
    
    public void setLastActivityDate(OffsetDateTime lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }
    
    public Boolean getAutoConverted() {
        return autoConverted;
    }
    
    public void setAutoConverted(Boolean autoConverted) {
        this.autoConverted = autoConverted;
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
