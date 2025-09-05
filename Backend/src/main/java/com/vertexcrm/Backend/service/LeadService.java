package com.vertexcrm.Backend.service;

import com.vertexcrm.Backend.DTO.Request.LeadRequestDto;
import com.vertexcrm.Backend.DTO.Response.LeadResponseDto;

import java.util.List;

public interface LeadService {
    List<LeadResponseDto> getAllLeads(Long orgId);
    LeadResponseDto addLead(LeadRequestDto leadDto);
    LeadResponseDto updateStatus(Long leadId, LeadRequestDto leadDto);
    LeadResponseDto getLeadById(Long leadId);
    LeadResponseDto updateLead(Long leadId, LeadRequestDto leadDto);
    LeadResponseDto deleteLead(Long leadId);
    List<LeadResponseDto> getLeadsByMember(Long memberId);
}
