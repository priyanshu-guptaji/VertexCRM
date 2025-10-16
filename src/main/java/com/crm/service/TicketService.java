package com.crm.service;

import com.crm.dto.TicketDto;
import com.crm.entity.Contact;
import com.crm.entity.Member;
import com.crm.entity.Organization;
import com.crm.entity.Ticket;
import com.crm.repository.ContactRepository;
import com.crm.repository.MemberRepository;
import com.crm.repository.OrganizationRepository;
import com.crm.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private MemberRepository memberRepository;

    public TicketDto create(TicketDto dto) {
        Organization org = organizationRepository.findById(dto.getOrgId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        Ticket t = new Ticket();
        apply(dto, t);
        t.setOrganization(org);
        if (dto.getRequesterId() != null) {
            Contact requester = contactRepository.findById(dto.getRequesterId())
                    .orElseThrow(() -> new RuntimeException("Requester not found"));
            t.setRequester(requester);
        }
        if (dto.getAssigneeId() != null) {
            Member assignee = memberRepository.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            t.setAssignee(assignee);
        }
        Ticket saved = ticketRepository.save(t);
        return toDto(saved);
    }

    public TicketDto update(Long id, TicketDto dto) {
        Ticket existing = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        if (dto.getOrgId() != null && (existing.getOrganization() == null ||
                !dto.getOrgId().equals(existing.getOrganization().getOrgId()))) {
            Organization org = organizationRepository.findById(dto.getOrgId())
                    .orElseThrow(() -> new RuntimeException("Organization not found"));
            existing.setOrganization(org);
        }
        if (dto.getRequesterId() != null) {
            Contact requester = contactRepository.findById(dto.getRequesterId())
                    .orElseThrow(() -> new RuntimeException("Requester not found"));
            existing.setRequester(requester);
        }
        if (dto.getAssigneeId() != null) {
            Member assignee = memberRepository.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            existing.setAssignee(assignee);
        }
        apply(dto, existing);
        Ticket updated = ticketRepository.save(existing);
        return toDto(updated);
    }

    public TicketDto get(Long id) {
        Ticket t = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        return toDto(t);
    }

    public List<TicketDto> listByOrg(Long orgId) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        return ticketRepository.findByOrganization(org)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("Ticket not found");
        }
        ticketRepository.deleteById(id);
    }

    private void apply(TicketDto dto, Ticket t) {
        if (dto.getSubject() != null) t.setSubject(dto.getSubject());
        if (dto.getDescription() != null) t.setDescription(dto.getDescription());
        if (dto.getStatus() != null) t.setStatus(dto.getStatus());
        if (dto.getPriority() != null) t.setPriority(dto.getPriority());
        if (dto.getSlaMinutes() != null) t.setSlaMinutes(dto.getSlaMinutes());
        if (dto.getDueAt() != null) {
            t.setDueAt(dto.getDueAt());
        } else if (dto.getSlaMinutes() != null) {
            OffsetDateTime base = t.getCreatedAt() != null ? t.getCreatedAt() : OffsetDateTime.now();
            t.setDueAt(base.plusMinutes(dto.getSlaMinutes()));
        }
    }

    private TicketDto toDto(Ticket t) {
        Long orgId = t.getOrganization() != null ? t.getOrganization().getOrgId() : null;
        Long requesterId = t.getRequester() != null ? t.getRequester().getContactId() : null;
        Long assigneeId = t.getAssignee() != null ? t.getAssignee().getMemberId() : null;
        return new TicketDto(
                t.getTicketId(),
                t.getSubject(),
                t.getDescription(),
                t.getStatus(),
                t.getPriority(),
                orgId,
                requesterId,
                assigneeId,
                t.getSlaMinutes(),
                t.getDueAt(),
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }
}
