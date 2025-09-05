package com.vertexcrm.Backend.service.Impl;

import com.vertexcrm.Backend.DTO.Request.LeadRequestDto;
import com.vertexcrm.Backend.DTO.Response.LeadResponseDto;
import com.vertexcrm.Backend.model.Lead;
import com.vertexcrm.Backend.model.Member;
import com.vertexcrm.Backend.model.Organization;
import com.vertexcrm.Backend.repository.LeadRepository;
import com.vertexcrm.Backend.repository.MemberRepository;
import com.vertexcrm.Backend.repository.OrganizationRepository;
import com.vertexcrm.Backend.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeadServiceImpl implements LeadService {

    @Autowired
    LeadRepository leadRepository;

    @Autowired
    OrganizationRepository orgRepository;

    @Autowired
    MemberRepository memberRepository;

    @Override
    public List<LeadResponseDto> getAllLeads(Long orgId) {
        List<Lead> lead = leadRepository.findByOrg_OrgId(orgId);
                return lead.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

//    add a lead
    @Override
    public LeadResponseDto addLead(LeadRequestDto leadDto) {
        Lead lead = new Lead();
        lead.setLeadName(leadDto.getLeadName());
        lead.setLeadEmail(leadDto.getLeadEmail());
        lead.setVerified(leadDto.isVerified());
        lead.setPhone(leadDto.getPhone());

        Organization org = orgRepository.findById(leadDto.getOrgId())
                .orElseThrow(() -> new RuntimeException("No Organization found" + leadDto.getOrgId()));
        lead.setOrg(org);

        Member member = memberRepository.findById(leadDto.getMemberId())
                .orElseThrow(() -> new RuntimeException("No Members found" + leadDto.getMemberId()));
        lead.setMember(member);

        Lead saved = leadRepository.save(lead);
        return mapToResponse(saved);
    }

//    Update lead
    @Override
    public LeadResponseDto updateLead(Long leadId, LeadRequestDto leadDto) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not Found" + leadId));
        lead.setLeadName(leadDto.getLeadName());
        lead.setLeadEmail(leadDto.getLeadEmail());
        lead.setPhone(lead.getPhone());

        Lead saved = leadRepository.save(lead);
        return mapToResponse(saved);
    }

//    update status
    @Override
    public LeadResponseDto updateStatus(Long leadId, LeadRequestDto leadDto) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found" + leadId));

        lead.setVerified(leadDto.isVerified());
        Lead saved = leadRepository.save(lead);

        return mapToResponse(saved);
    }

//    Get lead by Id
    @Override
    public LeadResponseDto getLeadById(Long leadId) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not Found"));
        return mapToResponse(lead);
    }

//    Get by member
    @Override
    public List<LeadResponseDto> getLeadsByMember(Long memberId) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new RuntimeException("Member not found" + memberId));
        List<Lead> leads = leadRepository.findByMember_MemberId(memberId);
        return leads.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

//    delete lead
    @Override
    public LeadResponseDto deleteLead(Long leadId) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found" + leadId));

        leadRepository.delete(lead);
        return mapToResponse(lead);
    }

    private LeadResponseDto mapToResponse(Lead lead) {
        LeadResponseDto response = new LeadResponseDto();
        response.setLeadId(lead.getLeadId());
        response.setLeadName(lead.getLeadName());
        response.setLeadEmail(lead.getLeadEmail());
        response.setVerified(lead.isVerified());
        response.setPhone(lead.getPhone());

        if(lead.getOrg()!=null) {
            response.setOrgName(lead.getOrg().getOrgName());
        }

        if(lead.getMember()!=null) {
            response.setMemberName(lead.getMember().getMemberName());
        }
        return response;
    }
}
