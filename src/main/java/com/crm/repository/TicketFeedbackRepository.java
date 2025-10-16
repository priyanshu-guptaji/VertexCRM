package com.crm.repository;

import com.crm.entity.TicketFeedback;
import com.crm.entity.Ticket;
import com.crm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketFeedbackRepository extends JpaRepository<TicketFeedback, Long> {
    Optional<TicketFeedback> findByTicket(Ticket ticket);
    List<TicketFeedback> findByOrganization(Organization organization);
    List<TicketFeedback> findByOrganizationAndSatisfied(Organization organization, Boolean satisfied);
    List<TicketFeedback> findByOrganizationAndRatingGreaterThanEqual(Organization organization, Integer minRating);
    
    @Query("SELECT AVG(tf.rating) FROM TicketFeedback tf WHERE tf.organization = :org")
    Double getAverageRatingByOrganization(@Param("org") Organization organization);
}
