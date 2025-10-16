package com.crm.repository;

import com.crm.entity.CampaignMetrics;
import com.crm.entity.Campaign;
import com.crm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignMetricsRepository extends JpaRepository<CampaignMetrics, Long> {
    Optional<CampaignMetrics> findByCampaign(Campaign campaign);
    List<CampaignMetrics> findByOrganization(Organization organization);
}
