package com.crm.dto;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonAlias;
=======
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class ActivityDto {
    private Long activityId;
    
    @NotBlank(message = "Activity type is required")
    @Size(max = 50, message = "Activity type must not exceed 50 characters")
<<<<<<< HEAD
    // Accept either "activityType" (frontend v1) or "type" (alt schema)
    @JsonAlias({"type", "activityType"})
=======
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
    private String activityType;
    
    @NotBlank(message = "Activity subject is required")
    @Size(max = 200, message = "Activity subject must not exceed 200 characters")
<<<<<<< HEAD
    // Accept either "subject" (frontend v1) or "name" (alt schema)
    @JsonAlias({"name", "subject"})
=======
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
    private String subject;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @NotNull(message = "Activity date is required")
<<<<<<< HEAD
    // Rely on Spring Boot's default ISO_LOCAL_DATE_TIME parsing for LocalDateTime
=======
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
    private LocalDateTime activityDate;
    
    @Size(max = 50, message = "Status must not exceed 50 characters")
    private String status;
    
    @Size(max = 50, message = "Priority must not exceed 50 characters")
    private String priority;
    
<<<<<<< HEAD
    // Removed @NotNull: controller populates orgId from JWT after validation would have run.
    // Validation that orgId is present is enforced in service instead to avoid 400 on create/update.
=======
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
    private Long orgId;
    
    private Long memberId;
    private Long leadId;
    private Long contactId;
    private Long accountId;
    private Long dealId;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime updatedAt;
    
    // Constructors
    public ActivityDto() {}
    
    public ActivityDto(String activityType, String subject, String description, 
                       LocalDateTime activityDate, String status, String priority, 
                       Long orgId, Long memberId, Long leadId, Long contactId, 
                       Long accountId, Long dealId) {
        this.activityType = activityType;
        this.subject = subject;
        this.description = description;
        this.activityDate = activityDate;
        this.status = status;
        this.priority = priority;
        this.orgId = orgId;
        this.memberId = memberId;
        this.leadId = leadId;
        this.contactId = contactId;
        this.accountId = accountId;
        this.dealId = dealId;
    }
    
    // Getters and Setters
    public Long getActivityId() {
        return activityId;
    }
    
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
    
    public String getActivityType() {
        return activityType;
    }
    
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getActivityDate() {
        return activityDate;
    }
    
    public void setActivityDate(LocalDateTime activityDate) {
        this.activityDate = activityDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public Long getOrgId() {
        return orgId;
    }
    
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
    
    public Long getMemberId() {
        return memberId;
    }
    
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    
    public Long getLeadId() {
        return leadId;
    }
    
    public void setLeadId(Long leadId) {
        this.leadId = leadId;
    }
    
    public Long getContactId() {
        return contactId;
    }
    
    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
    
    public Long getAccountId() {
        return accountId;
    }
    
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    
    public Long getDealId() {
        return dealId;
    }
    
    public void setDealId(Long dealId) {
        this.dealId = dealId;
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
