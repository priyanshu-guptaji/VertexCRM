package com.crm.controller;

import com.crm.dto.SupportRequestDto;
import com.crm.entity.Member;
import com.crm.entity.Organization;
import com.crm.repository.MemberRepository;
import com.crm.repository.OrganizationRepository;
import com.crm.service.EmailService;
import com.crm.util.AuthenticationUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/support")
@CrossOrigin(origins = "*")
public class SupportController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationUtils authenticationUtils;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    /**
     * Endpoint used by the user dashboard contact form.
     * Sends an email via the configured SMTP server to the support address.
     */
    @PostMapping("/contact")
    @PreAuthorize("hasAnyRole('Admin','Manager','Sales Rep','User')")
    public ResponseEntity<?> sendSupportRequest(@Valid @RequestBody SupportRequestDto request,
                                                Authentication authentication,
                                                HttpServletRequest httpRequest) {
        try {
            Long orgId = authenticationUtils.getOrgIdFromAuthentication(authentication, httpRequest);
            Long memberId = authenticationUtils.getMemberIdFromAuthentication(authentication, httpRequest);

            Organization organization = organizationRepository.findById(orgId)
                    .orElse(null);
            Member member = memberRepository.findById(memberId)
                    .orElse(null);

            emailService.sendSupportRequest(request, member, organization);

            return ResponseEntity.ok("Support request sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending support request: " + e.getMessage());
        }
    }
}
