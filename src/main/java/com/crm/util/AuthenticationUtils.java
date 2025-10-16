package com.crm.util;

import com.crm.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthenticationUtils {
    
    @Autowired
    private JwtConfig jwtConfig;
    
    public Long getOrgIdFromAuthentication(Authentication authentication, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null) {
            try {
                return jwtConfig.extractOrgId(token);
            } catch (Exception e) {
                // Fallback to default if token extraction fails
                return 1L;
            }
        }
        return 1L; // Default fallback
    }
    
    public Long getMemberIdFromAuthentication(Authentication authentication, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null) {
            try {
                return jwtConfig.extractMemberId(token);
            } catch (Exception e) {
                // Fallback to default if token extraction fails
                return 1L;
            }
        }
        return 1L; // Default fallback
    }
    
    private String extractTokenFromRequest(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            return requestTokenHeader.substring(7);
        }
        return null;
    }
    
    /**
     * Get current organization ID from request context
     */
    public Long getCurrentOrgId() {
        HttpServletRequest request = getCurrentRequest();
        if (request != null) {
            String token = extractTokenFromRequest(request);
            if (token != null) {
                try {
                    return jwtConfig.extractOrgId(token);
                } catch (Exception e) {
                    return 1L;
                }
            }
        }
        return 1L;
    }
    
    /**
     * Get current member ID from request context
     */
    public Long getCurrentMemberId() {
        HttpServletRequest request = getCurrentRequest();
        if (request != null) {
            String token = extractTokenFromRequest(request);
            if (token != null) {
                try {
                    return jwtConfig.extractMemberId(token);
                } catch (Exception e) {
                    return 1L;
                }
            }
        }
        return 1L;
    }
    
    /**
     * Get current HTTP request from context
     */
    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}

