package com.vertexcrm.Backend.controller;

import com.vertexcrm.Backend.DTO.Request.LeadRequestDto;
import com.vertexcrm.Backend.DTO.Response.LeadResponseDto;
import com.vertexcrm.Backend.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LeadController {

    @Autowired
    LeadService leadService;

    @GetMapping("/org/{orgId}/leads")
    public ResponseEntity<List<LeadResponseDto>> getAllLeads(@PathVariable Long orgId) {
        return new ResponseEntity<>(leadService.getAllLeads(orgId), HttpStatus.OK);
    }

    @PostMapping("/org/lead")
    public ResponseEntity<LeadResponseDto> addLead(@RequestBody LeadRequestDto leadDto) {
        return new ResponseEntity<>(leadService.addLead(leadDto), HttpStatus.OK);
    }

//    get lead by id
    @GetMapping("/org/lead/{leadId}")
    public ResponseEntity<LeadResponseDto> getLeadById(@PathVariable Long leadId) {
        return new ResponseEntity<>(leadService.getLeadById(leadId), HttpStatus.OK);
    }

//    Update Lead info
    @PutMapping("/org/lead/{leadId}")
    public ResponseEntity<LeadResponseDto> updateLead(@PathVariable Long leadId,
                                                      @RequestBody LeadRequestDto leadDto) {
        return new ResponseEntity<>(leadService.updateLead(leadId, leadDto), HttpStatus.OK);
    }

//    Update Status
    @PutMapping("/org/lead/{leadId}/status")
    public ResponseEntity<LeadResponseDto> updateStatus(@PathVariable Long leadId,
                                                        @RequestBody LeadRequestDto leadDto) {
        return new ResponseEntity<>(leadService.updateStatus(leadId, leadDto), HttpStatus.OK);
    }

    //    get leads of members
    @GetMapping("/member/{memberId}/leads")
    public ResponseEntity<List<LeadResponseDto>> getLeadsByMember(@PathVariable Long memberId) {
        return new ResponseEntity<>(leadService.getLeadsByMember(memberId), HttpStatus.OK);
    }

//    delete lead
    @DeleteMapping("/org/lead/{leadId}")
    public ResponseEntity<LeadResponseDto> deleteLead(@PathVariable Long leadId) {
        return new ResponseEntity<>(leadService.deleteLead(leadId), HttpStatus.OK);
    }
}
