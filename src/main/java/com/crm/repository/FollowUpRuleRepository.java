package com.crm.repository;

import com.crm.entity.FollowUpRule;
import com.crm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowUpRuleRepository extends JpaRepository<FollowUpRule, Long> {
    List<FollowUpRule> findByOrganization(Organization organization);
    List<FollowUpRule> findByOrganizationAndIsActive(Organization organization, Boolean isActive);
    List<FollowUpRule> findByOrganizationAndEntityType(Organization organization, String entityType);
    List<FollowUpRule> findByOrganizationAndEntityTypeAndIsActive(Organization organization, String entityType, Boolean isActive);
}
