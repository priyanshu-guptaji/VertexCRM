package com.crm.security;

import com.crm.config.JwtConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        final String requestTokenHeader = request.getHeader("Authorization");
        final String requestURI = request.getRequestURI();
        
        logger.debug("JWT Filter - Processing request: " + request.getMethod() + " " + requestURI);
        logger.debug("JWT Filter - Authorization header: " + (requestTokenHeader != null ? "Present" : "Missing"));

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            logger.debug("JWT Filter - Token extracted, length: " + jwtToken.length());
            try {
                username = jwtConfig.extractUsername(jwtToken);
                logger.debug("JWT Filter - Username extracted: " + username);
            } catch (Exception e) {
                logger.error("JWT Filter - Unable to get JWT Token or JWT Token has expired: " + e.getMessage());
            }
        } else {
            logger.debug("JWT Filter - No valid Authorization header found");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.debug("JWT Filter - Validating token for user: " + username);
            try {
                if (jwtConfig.validateToken(jwtToken, username)) {
<<<<<<< HEAD
                    // Extract role from token and create authorities
                    String role = jwtConfig.extractRole(jwtToken);
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
=======
                    // Extract role from token and create authorities (normalize to expected Spring format)
                    String role = jwtConfig.extractRole(jwtToken);
                    String roleUpper = role != null ? role.toUpperCase() : "";
                    // Map common role names to consistent identifiers without spaces
                    String normalizedRole;
                    if (roleUpper.startsWith("ADMIN")) {
                        normalizedRole = "ADMIN";
                    } else if (roleUpper.startsWith("MANAGER")) {
                        normalizedRole = "MANAGER";
                    } else if (roleUpper.contains("SALES")) {
                        normalizedRole = "SALES";
                    } else if (roleUpper.startsWith("USER")) {
                        normalizedRole = "USER";
                    } else {
                        // Fallback: remove non-alphanumerics and spaces
                        normalizedRole = roleUpper.replaceAll("[^A-Z0-9]", "");
                    }
                    Long orgId = jwtConfig.extractOrgId(jwtToken);
                    Long memberId = jwtConfig.extractMemberId(jwtToken);
                    TenantContext.setRole(normalizedRole);
                    TenantContext.setOrgId(orgId);
                    TenantContext.setMemberId(memberId);
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + normalizedRole));
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
                    
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
<<<<<<< HEAD
                    logger.debug("JWT Filter - Authentication set for user: " + username + " with role: " + role);
=======
                    logger.debug("JWT Filter - Authentication set for user: " + username + " with role: " + roleUpper);
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
                } else {
                    logger.warn("JWT Filter - Token validation failed for user: " + username);
                }
            } catch (Exception e) {
                logger.error("JWT Filter - Error validating JWT token: " + e.getMessage(), e);
            }
        } else if (username == null) {
            logger.debug("JWT Filter - No username extracted, request will be anonymous");
        } else {
            logger.debug("JWT Filter - Authentication already exists in SecurityContext");
        }
        
<<<<<<< HEAD
        filterChain.doFilter(request, response);
=======
        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
    }
}
