package com.crm.repository;

import com.crm.entity.Campaign;
import com.crm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByOrganization(Organization organization);
}
