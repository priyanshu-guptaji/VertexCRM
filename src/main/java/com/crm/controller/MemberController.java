package com.crm.controller;

import com.crm.dto.MemberDto;
import com.crm.service.MemberService;
import jakarta.validation.Valid;
<<<<<<< HEAD
=======
import com.crm.util.AuthenticationUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*")
<<<<<<< HEAD
=======
@org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
public class MemberController {
    
    @Autowired
    private MemberService memberService;
    
<<<<<<< HEAD
=======
    @Autowired
    private AuthenticationUtils authenticationUtils;
    
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
    @PostMapping
    public ResponseEntity<?> createMember(@Valid @RequestBody MemberDto memberDto) {
        try {
            MemberDto createdMember = memberService.createMember(memberDto);
            return ResponseEntity.ok(createdMember);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping
<<<<<<< HEAD
    public ResponseEntity<?> getAllMembers() {
        try {
            List<MemberDto> members = memberService.getAllMembers();
=======
    public ResponseEntity<?> getMembersByOrganization(Authentication authentication, HttpServletRequest request) {
        try {
            Long orgId = authenticationUtils.getOrgIdFromAuthentication(authentication, request);
            List<MemberDto> members = memberService.getMembersByOrganization(orgId);
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getMemberById(@PathVariable Long memberId) {
        try {
            MemberDto member = memberService.getMemberById(memberId);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{memberId}")
    public ResponseEntity<?> updateMember(@PathVariable Long memberId, @Valid @RequestBody MemberDto memberDto) {
        try {
            MemberDto updatedMember = memberService.updateMember(memberId, memberDto);
            return ResponseEntity.ok(updatedMember);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Long memberId) {
        try {
            memberService.deleteMember(memberId);
            return ResponseEntity.ok("Member deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

<<<<<<< HEAD


=======
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
