package com.crm.repository;

import com.crm.entity.Member;
import com.crm.entity.Organization;
import com.crm.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByOrganization(Organization organization);
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.assignee = :assignee AND t.status = :status")
    long countByAssigneeAndStatus(@Param("assignee") Member assignee, @Param("status") String status);
    
    List<Ticket> findByOrganizationAndStatus(Organization organization, String status);
    
    // Additional queries for automation
    @Query("SELECT t FROM Ticket t WHERE t.firstResponseDue IS NOT NULL OR t.resolutionDue IS NOT NULL")
    List<Ticket> findTicketsNeedingSLAMonitoring();
}