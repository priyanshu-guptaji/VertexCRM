package com.crm.repository;

import com.crm.entity.DripCampaignStep;
import com.crm.entity.DripCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DripCampaignStepRepository extends JpaRepository<DripCampaignStep, Long> {
    List<DripCampaignStep> findByDripCampaign(DripCampaign dripCampaign);
    List<DripCampaignStep> findByDripCampaignOrderByStepOrderAsc(DripCampaign dripCampaign);
    List<DripCampaignStep> findByDripCampaignAndIsActive(DripCampaign dripCampaign, Boolean isActive);
}
