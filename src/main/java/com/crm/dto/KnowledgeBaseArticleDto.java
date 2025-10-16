package com.crm.dto;

import java.time.OffsetDateTime;

public class KnowledgeBaseArticleDto {
    private Long articleId;
    private String title;
    private String content;
    private String category;
    private String visibility;
    private Long orgId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public KnowledgeBaseArticleDto() {}

    public KnowledgeBaseArticleDto(Long articleId, String title, String content, String category,
                                   String visibility, Long orgId, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.visibility = visibility;
        this.orgId = orgId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
