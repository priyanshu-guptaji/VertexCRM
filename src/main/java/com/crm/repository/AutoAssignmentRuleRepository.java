package com.crm.repository;

import com.crm.entity.AutoAssignmentRule;
import com.crm.entity.TicketCategory;
import com.crm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoAssignmentRuleRepository extends JpaRepository<AutoAssignmentRule, Long> {
    List<AutoAssignmentRule> findByOrganization(Organization organization);
    List<AutoAssignmentRule> findByOrganizationAndIsActive(Organization organization, Boolean isActive);
    List<AutoAssignmentRule> findByOrganizationAndCategoryAndIsActive(Organization organization, TicketCategory category, Boolean isActive);
    List<AutoAssignmentRule> findByOrganizationAndIsActiveOrderByRulePriorityDesc(Organization organization, Boolean isActive);
}
