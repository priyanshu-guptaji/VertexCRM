package com.crm.controller;

import com.crm.dto.OrganizationDto;
import com.crm.service.OrganizationService;
<<<<<<< HEAD
=======
import com.crm.util.AuthenticationUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@CrossOrigin(origins = "*")
<<<<<<< HEAD
=======
@org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
public class OrganizationController {
    
    @Autowired
    private OrganizationService organizationService;
    
<<<<<<< HEAD
=======
    @Autowired
    private AuthenticationUtils authenticationUtils;
    
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
    @PostMapping
    public ResponseEntity<?> createOrganization(@Valid @RequestBody OrganizationDto organizationDto) {
        try {
            OrganizationDto createdOrganization = organizationService.createOrganization(organizationDto);
            return ResponseEntity.ok(createdOrganization);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping
<<<<<<< HEAD
    public ResponseEntity<?> getAllOrganizations() {
        try {
            List<OrganizationDto> organizations = organizationService.getAllOrganizations();
            return ResponseEntity.ok(organizations);
=======
    public ResponseEntity<?> getMyOrganization(Authentication authentication, HttpServletRequest request) {
        try {
            Long orgId = authenticationUtils.getOrgIdFromAuthentication(authentication, request);
            OrganizationDto organization = organizationService.getOrganizationById(orgId);
            return ResponseEntity.ok(java.util.List.of(organization));
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{orgId}")
    public ResponseEntity<?> getOrganizationById(@PathVariable Long orgId) {
        try {
            OrganizationDto organization = organizationService.getOrganizationById(orgId);
            return ResponseEntity.ok(organization);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{orgId}")
    public ResponseEntity<?> updateOrganization(@PathVariable Long orgId, @Valid @RequestBody OrganizationDto organizationDto) {
        try {
            OrganizationDto updatedOrganization = organizationService.updateOrganization(orgId, organizationDto);
            return ResponseEntity.ok(updatedOrganization);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{orgId}")
    public ResponseEntity<?> deleteOrganization(@PathVariable Long orgId) {
        try {
            organizationService.deleteOrganization(orgId);
            return ResponseEntity.ok("Organization deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

<<<<<<< HEAD


=======
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
