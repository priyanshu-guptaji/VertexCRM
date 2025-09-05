package com.vertexcrm.Backend.service;

import com.vertexcrm.Backend.DTO.Request.OrganizationRequestDto;
import com.vertexcrm.Backend.DTO.Response.OrganizationResponseDto;

import java.util.List;

public interface OrganizationService {
    OrganizationResponseDto addOrg(OrganizationRequestDto orgDto);
    List<OrganizationResponseDto> getOrgs();
    OrganizationResponseDto getOrgById(Long id);
    OrganizationResponseDto updateOrg(OrganizationRequestDto orgDto, Long id);
    OrganizationResponseDto deleteOrg(Long id);
}
