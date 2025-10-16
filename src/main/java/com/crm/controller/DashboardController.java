package com.crm.controller;

import com.crm.entity.Organization;
import com.crm.repository.CampaignRepository;
import com.crm.repository.DealRepository;
import com.crm.repository.OrganizationRepository;
import com.crm.repository.TicketRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboards")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DealRepository dealRepository;
    private final CampaignRepository campaignRepository;
    private final TicketRepository ticketRepository;
    private final OrganizationRepository organizationRepository;

    public DashboardController(DealRepository dealRepository, CampaignRepository campaignRepository,
                               TicketRepository ticketRepository, OrganizationRepository organizationRepository) {
        this.dealRepository = dealRepository;
        this.campaignRepository = campaignRepository;
        this.ticketRepository = ticketRepository;
        this.organizationRepository = organizationRepository;
    }

    @GetMapping("/org/{orgId}/overview")
    public ResponseEntity<?> overview(@PathVariable Long orgId) {
        try {
            Organization org = organizationRepository.findById(orgId).orElseThrow();
            var deals = dealRepository.findByOrganization(org);
            var tickets = ticketRepository.findByOrganization(org);
            var campaigns = campaignRepository.findByOrganization(org);

            BigDecimal pipeline = deals.stream().map(d -> d.getDealValue() == null ? BigDecimal.ZERO : d.getDealValue())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            long openDeals = deals.stream().filter(d -> d.getDealStage() != null && !d.getDealStage().toLowerCase().contains("closed")).count();
            long closedDeals = deals.stream().filter(d -> d.getDealStage() != null && d.getDealStage().toLowerCase().contains("closed")).count();

            BigDecimal revenue = campaigns.stream().map(c -> c.getRevenue() == null ? BigDecimal.ZERO : c.getRevenue())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal cost = campaigns.stream().map(c -> c.getCost() == null ? BigDecimal.ZERO : c.getCost())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            long openTickets = tickets.stream().filter(t -> t.getStatus() == null || !t.getStatus().equalsIgnoreCase("closed")).count();
            long closedTickets = tickets.stream().filter(t -> t.getStatus() != null && t.getStatus().equalsIgnoreCase("closed")).count();

            Map<String, Object> out = new HashMap<>();
            out.put("pipelineValue", pipeline);
            out.put("openDeals", openDeals);
            out.put("closedDeals", closedDeals);
            out.put("campaignRevenue", revenue);
            out.put("campaignCost", cost);
            out.put("openTickets", openTickets);
            out.put("closedTickets", closedTickets);
            return ResponseEntity.ok(out);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
