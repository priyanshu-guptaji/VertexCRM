package com.crm.repository;

import com.crm.entity.LeadScore;
import com.crm.entity.Lead;
import com.crm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeadScoreRepository extends JpaRepository<LeadScore, Long> {
    Optional<LeadScore> findByLead(Lead lead);
    List<LeadScore> findByOrganization(Organization organization);
    List<LeadScore> findByOrganizationAndGrade(Organization organization, String grade);
    List<LeadScore> findByOrganizationAndTotalScoreGreaterThanEqual(Organization organization, Integer minScore);
    List<LeadScore> findByOrganizationAndAutoConverted(Organization organization, Boolean autoConverted);
    
    @Query("SELECT ls FROM LeadScore ls WHERE ls.organization = :organization AND ls.totalScore >= :minScore ORDER BY ls.totalScore DESC")
    List<LeadScore> findTopLeadsByScore(@Param("organization") Organization organization, @Param("minScore") Integer minScore);
    
    @Query("SELECT ls FROM LeadScore ls WHERE ls.organization = :organization AND ls.autoConverted = false AND ls.totalScore >= :threshold")
    List<LeadScore> findEligibleForAutoConversion(@Param("organization") Organization organization, @Param("threshold") Integer threshold);
}
