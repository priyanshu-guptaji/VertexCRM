package com.crm.repository;

import com.crm.entity.TicketCategory;
import com.crm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {
    List<TicketCategory> findByOrganization(Organization organization);
    List<TicketCategory> findByOrganizationAndIsActive(Organization organization, Boolean isActive);
}
