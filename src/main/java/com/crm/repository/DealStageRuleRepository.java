package com.crm.repository;

import com.crm.entity.DealStageRule;
import com.crm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealStageRuleRepository extends JpaRepository<DealStageRule, Long> {
    List<DealStageRule> findByOrganization(Organization organization);
    List<DealStageRule> findByOrganizationAndIsActive(Organization organization, Boolean isActive);
    List<DealStageRule> findByOrganizationAndSourceStage(Organization organization, String sourceStage);
    List<DealStageRule> findByOrganizationAndSourceStageAndTriggerType(Organization organization, String sourceStage, String triggerType);
    List<DealStageRule> findByOrganizationAndIsActiveOrderByPriorityDesc(Organization organization, Boolean isActive);
}
