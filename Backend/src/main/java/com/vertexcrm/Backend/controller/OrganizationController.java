package com.vertexcrm.Backend.controller;

import com.vertexcrm.Backend.DTO.Request.OrganizationRequestDto;
import com.vertexcrm.Backend.DTO.Response.OrganizationResponseDto;
import com.vertexcrm.Backend.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrganizationController {

    @Autowired
    private OrganizationService orgService;

    //    Create Organization
    @PostMapping("/org")
    public ResponseEntity<OrganizationResponseDto> addOrg(@RequestBody OrganizationRequestDto orgDto) {
        return new ResponseEntity<>(orgService.addOrg(orgDto), HttpStatus.OK);
    }

    @GetMapping("/org")
    public ResponseEntity<List<OrganizationResponseDto>> getOrgs() {
        return new ResponseEntity<>(orgService.getOrgs(), HttpStatus.OK);
    }

    @GetMapping("/org/{id}")
    public ResponseEntity<OrganizationResponseDto> getOrgById(@PathVariable Long id) {
        OrganizationResponseDto org = orgService.getOrgById(id);
        if(org != null) {
            return new ResponseEntity<>(org, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    Update Organization
    @PutMapping("/org/{id}")
    public ResponseEntity<OrganizationResponseDto> updateOrg(@RequestBody OrganizationRequestDto orgDto,
                                                             @PathVariable Long id) {
        OrganizationResponseDto updatedOrg = orgService.updateOrg(orgDto, id);
        return new ResponseEntity<>(updatedOrg, HttpStatus.OK);
    }

//    Delete Organization
    @DeleteMapping("/org/{id}")
    public ResponseEntity<OrganizationResponseDto> deleteOrg(@PathVariable Long id) {
        OrganizationResponseDto orgDto = orgService.deleteOrg(id);

        if(orgDto != null) {
            return new ResponseEntity<>(orgDto, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
