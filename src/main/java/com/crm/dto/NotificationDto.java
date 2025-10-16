package com.crm.dto;

import java.time.OffsetDateTime;

public class NotificationDto {
    private Long notificationId;
    private Long orgId;
    private Long memberId;
    private String type;
    private String title;
    private String message;
    private Boolean isRead;
    private OffsetDateTime createdAt;

    public NotificationDto() {}

    public NotificationDto(Long notificationId, Long orgId, Long memberId, String type, String title, String message, Boolean isRead, OffsetDateTime createdAt) {
        this.notificationId = notificationId;
        this.orgId = orgId;
        this.memberId = memberId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Long getNotificationId() { return notificationId; }
    public void setNotificationId(Long notificationId) { this.notificationId = notificationId; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Boolean getRead() { return isRead; }
    public void setRead(Boolean read) { isRead = read; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
