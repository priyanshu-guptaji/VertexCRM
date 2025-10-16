package com.crm.repository;

import com.crm.entity.SlaPolicy;
import com.crm.entity.TicketCategory;
import com.crm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SlaPolicyRepository extends JpaRepository<SlaPolicy, Long> {
    List<SlaPolicy> findByOrganization(Organization organization);
    List<SlaPolicy> findByOrganizationAndIsActive(Organization organization, Boolean isActive);
    Optional<SlaPolicy> findByOrganizationAndCategoryAndPriority(Organization organization, TicketCategory category, String priority);
    
    @Query("SELECT s FROM SlaPolicy s WHERE s.organization = :org AND s.category = :category AND s.isActive = true ORDER BY s.priority")
    List<SlaPolicy> findActivePoliciesByCategory(@Param("org") Organization organization, @Param("category") TicketCategory category);
}
