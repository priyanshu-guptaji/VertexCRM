package com.crm.util;

import com.crm.security.TenantContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthenticationUtils {
    
    public Long getOrgIdFromAuthentication(Authentication authentication, HttpServletRequest request) {
        Long orgId = TenantContext.getOrgId();
        if (orgId == null) {
            throw new IllegalStateException("Missing tenant context (orgId)");
        }
        return orgId;
    }
    
    public Long getMemberIdFromAuthentication(Authentication authentication, HttpServletRequest request) {
        Long memberId = TenantContext.getMemberId();
        if (memberId == null) {
            throw new IllegalStateException("Missing tenant context (memberId)");
        }
        return memberId;
    }
}

