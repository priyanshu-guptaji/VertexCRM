package com.crm.service;

import com.crm.dto.OrganizationDto;
import com.crm.entity.Organization;
import com.crm.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationService {
    
    @Autowired
    private OrganizationRepository organizationRepository;
<<<<<<< HEAD

    @Autowired
    private TenantSchemaService tenantSchemaService;
=======
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
    
    public OrganizationDto createOrganization(OrganizationDto organizationDto) {
        if (organizationRepository.existsByOrgEmail(organizationDto.getOrgEmail())) {
            throw new RuntimeException("Organization email already exists");
        }
        
        Organization organization = new Organization();
        organization.setOrgName(organizationDto.getOrgName());
        organization.setOrgEmail(organizationDto.getOrgEmail());
        
        Organization savedOrganization = organizationRepository.save(organization);
<<<<<<< HEAD

        // Provision tenant schema derived from org name (fallback to email local part)
        String base = (organizationDto.getOrgName() != null && !organizationDto.getOrgName().isBlank())
                ? organizationDto.getOrgName()
                : (organizationDto.getOrgEmail() != null ? organizationDto.getOrgEmail().split("@")[0] : "public");
        String schema = base.trim().toLowerCase().replaceAll("[^a-z0-9_]+", "_");
        tenantSchemaService.provisionSchema(schema);
=======
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
        return convertToDto(savedOrganization);
    }
    
    public List<OrganizationDto> getAllOrganizations() {
        return organizationRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public OrganizationDto getOrganizationById(Long orgId) {
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        return convertToDto(organization);
    }
    
    public OrganizationDto updateOrganization(Long orgId, OrganizationDto organizationDto) {
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        organization.setOrgName(organizationDto.getOrgName());
        organization.setOrgEmail(organizationDto.getOrgEmail());
        
        Organization savedOrganization = organizationRepository.save(organization);
        return convertToDto(savedOrganization);
    }
    
    public void deleteOrganization(Long orgId) {
        if (!organizationRepository.existsById(orgId)) {
            throw new RuntimeException("Organization not found");
        }
        organizationRepository.deleteById(orgId);
    }
    
    private OrganizationDto convertToDto(Organization organization) {
        return new OrganizationDto(
            organization.getOrgId(),
            organization.getOrgName(),
            organization.getOrgEmail(),
            organization.getCreatedAt(),
            organization.getUpdatedAt()
        );
    }
}
