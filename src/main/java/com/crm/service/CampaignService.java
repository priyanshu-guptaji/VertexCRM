package com.crm.service;

import com.crm.dto.CampaignDto;
import com.crm.entity.Campaign;
import com.crm.entity.Organization;
import com.crm.repository.CampaignRepository;
import com.crm.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    public CampaignDto create(CampaignDto dto) {
        Organization org = organizationRepository.findById(dto.getOrgId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        Campaign c = toEntity(dto, new Campaign());
        c.setOrganization(org);
        Campaign saved = campaignRepository.save(c);
        return toDto(saved);
    }

    public CampaignDto update(Long id, CampaignDto dto) {
        Campaign existing = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
        if (dto.getOrgId() != null && (existing.getOrganization() == null ||
                !dto.getOrgId().equals(existing.getOrganization().getOrgId()))) {
            Organization org = organizationRepository.findById(dto.getOrgId())
                    .orElseThrow(() -> new RuntimeException("Organization not found"));
            existing.setOrganization(org);
        }
        Campaign updated = campaignRepository.save(toEntity(dto, existing));
        return toDto(updated);
    }

    public CampaignDto get(Long id) {
        Campaign c = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
        return toDto(c);
    }

    public List<CampaignDto> listByOrg(Long orgId) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        return campaignRepository.findByOrganization(org)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!campaignRepository.existsById(id)) {
            throw new RuntimeException("Campaign not found");
        }
        campaignRepository.deleteById(id);
    }

    private Campaign toEntity(CampaignDto dto, Campaign c) {
        if (dto.getName() != null) c.setName(dto.getName());
        if (dto.getType() != null) c.setType(dto.getType());
        if (dto.getStatus() != null) c.setStatus(dto.getStatus());
        if (dto.getStartAt() != null) c.setStartAt(dto.getStartAt());
        if (dto.getEndAt() != null) c.setEndAt(dto.getEndAt());
        if (dto.getBudget() != null) c.setBudget(dto.getBudget());
        if (dto.getCost() != null) c.setCost(dto.getCost());
        if (dto.getRevenue() != null) c.setRevenue(dto.getRevenue());
        return c;
    }

    private CampaignDto toDto(Campaign c) {
        Long orgId = c.getOrganization() != null ? c.getOrganization().getOrgId() : null;
        return new CampaignDto(
                c.getCampaignId(),
                c.getName(),
                c.getType(),
                c.getStatus(),
                c.getStartAt(),
                c.getEndAt(),
                c.getBudget(),
                c.getCost(),
                c.getRevenue(),
                orgId,
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}
