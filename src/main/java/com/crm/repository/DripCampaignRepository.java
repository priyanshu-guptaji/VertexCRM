package com.crm.repository;

import com.crm.entity.DripCampaign;
import com.crm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DripCampaignRepository extends JpaRepository<DripCampaign, Long> {
    List<DripCampaign> findByOrganization(Organization organization);
    List<DripCampaign> findByOrganizationAndIsActive(Organization organization, Boolean isActive);
    List<DripCampaign> findByOrganizationAndTriggerType(Organization organization, String triggerType);
    
    @Query("SELECT dc FROM DripCampaign dc LEFT JOIN FETCH dc.steps WHERE dc.dripCampaignId = :id")
    DripCampaign findByIdWithSteps(@Param("id") Long id);
}
