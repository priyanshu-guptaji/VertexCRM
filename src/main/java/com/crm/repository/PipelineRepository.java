package com.crm.repository;

import com.crm.entity.Pipeline;
import com.crm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PipelineRepository extends JpaRepository<Pipeline, Long> {
    List<Pipeline> findByOrganization(Organization organization);
    List<Pipeline> findByOrganizationAndIsActive(Organization organization, Boolean isActive);
    Optional<Pipeline> findByOrganizationAndIsDefault(Organization organization, Boolean isDefault);
}
