package com.crm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.OffsetDateTime;

public class LeadScoreDto {
    
    private Long scoreId;
    private Long leadId;
    private String leadName;
    private String leadEmail;
    private Integer engagementScore;
    private Integer demographicScore;
    private Integer behaviorScore;
    private Integer totalScore;
    private String grade;
    private Integer emailOpens;
    private Integer emailClicks;
    private Integer websiteVisits;
    private Integer formSubmissions;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime lastActivityDate;
    
    private Boolean autoConverted;
    private Long orgId;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime updatedAt;
    
    // Constructors
    public LeadScoreDto() {}
    
    // Getters and Setters
    public Long getScoreId() {
        return scoreId;
    }
    
    public void setScoreId(Long scoreId) {
        this.scoreId = scoreId;
    }
    
    public Long getLeadId() {
        return leadId;
    }
    
    public void setLeadId(Long leadId) {
        this.leadId = leadId;
    }
    
    public String getLeadName() {
        return leadName;
    }
    
    public void setLeadName(String leadName) {
        this.leadName = leadName;
    }
    
    public String getLeadEmail() {
        return leadEmail;
    }
    
    public void setLeadEmail(String leadEmail) {
        this.leadEmail = leadEmail;
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
    
    public Long getOrgId() {
        return orgId;
    }
    
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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
