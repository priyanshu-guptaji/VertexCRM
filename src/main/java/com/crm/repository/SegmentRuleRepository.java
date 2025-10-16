package com.crm.repository;

import com.crm.entity.SegmentRule;
import com.crm.entity.Segment;
import com.crm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegmentRuleRepository extends JpaRepository<SegmentRule, Long> {
    List<SegmentRule> findBySegment(Segment segment);
    List<SegmentRule> findByOrganization(Organization organization);
}
