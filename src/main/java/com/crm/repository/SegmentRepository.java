package com.crm.repository;

import com.crm.entity.Organization;
import com.crm.entity.Segment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SegmentRepository extends JpaRepository<Segment, Long> {
    List<Segment> findByOrganization(Organization organization);
}
