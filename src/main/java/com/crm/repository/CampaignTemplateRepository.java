package com.crm.repository;

import com.crm.entity.CampaignTemplate;
import com.crm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignTemplateRepository extends JpaRepository<CampaignTemplate, Long> {
    List<CampaignTemplate> findByOrganization(Organization organization);
    List<CampaignTemplate> findByOrganizationAndIsActive(Organization organization, Boolean isActive);
    List<CampaignTemplate> findByOrganizationAndChannelType(Organization organization, String channelType);
}
