package com.crm.service;

import com.crm.dto.KnowledgeBaseArticleDto;
import com.crm.entity.KnowledgeBaseArticle;
import com.crm.entity.Organization;
import com.crm.repository.KnowledgeBaseArticleRepository;
import com.crm.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KnowledgeBaseService {

    @Autowired
    private KnowledgeBaseArticleRepository kbRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    public KnowledgeBaseArticleDto create(KnowledgeBaseArticleDto dto) {
        Organization org = organizationRepository.findById(dto.getOrgId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        KnowledgeBaseArticle a = apply(dto, new KnowledgeBaseArticle());
        a.setOrganization(org);
        KnowledgeBaseArticle saved = kbRepository.save(a);
        return toDto(saved);
    }

    public KnowledgeBaseArticleDto update(Long id, KnowledgeBaseArticleDto dto) {
        KnowledgeBaseArticle existing = kbRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        if (dto.getOrgId() != null && (existing.getOrganization() == null ||
                !dto.getOrgId().equals(existing.getOrganization().getOrgId()))) {
            Organization org = organizationRepository.findById(dto.getOrgId())
                    .orElseThrow(() -> new RuntimeException("Organization not found"));
            existing.setOrganization(org);
        }
        KnowledgeBaseArticle updated = kbRepository.save(apply(dto, existing));
        return toDto(updated);
    }

    public KnowledgeBaseArticleDto get(Long id) {
        KnowledgeBaseArticle a = kbRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        return toDto(a);
    }

    public List<KnowledgeBaseArticleDto> listByOrg(Long orgId) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        return kbRepository.findByOrganization(org)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!kbRepository.existsById(id)) {
            throw new RuntimeException("Article not found");
        }
        kbRepository.deleteById(id);
    }

    private KnowledgeBaseArticle apply(KnowledgeBaseArticleDto dto, KnowledgeBaseArticle a) {
        if (dto.getTitle() != null) a.setTitle(dto.getTitle());
        if (dto.getContent() != null) a.setContent(dto.getContent());
        if (dto.getCategory() != null) a.setCategory(dto.getCategory());
        if (dto.getVisibility() != null) a.setVisibility(dto.getVisibility());
        return a;
    }

    private KnowledgeBaseArticleDto toDto(KnowledgeBaseArticle a) {
        Long orgId = a.getOrganization() != null ? a.getOrganization().getOrgId() : null;
        return new KnowledgeBaseArticleDto(
                a.getArticleId(),
                a.getTitle(),
                a.getContent(),
                a.getCategory(),
                a.getVisibility(),
                orgId,
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }
}
