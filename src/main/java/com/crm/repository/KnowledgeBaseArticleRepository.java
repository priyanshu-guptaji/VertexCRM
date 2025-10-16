package com.crm.repository;

import com.crm.entity.KnowledgeBaseArticle;
import com.crm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KnowledgeBaseArticleRepository extends JpaRepository<KnowledgeBaseArticle, Long> {
    List<KnowledgeBaseArticle> findByOrganization(Organization organization);
}
