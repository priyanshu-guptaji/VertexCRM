package com.crm.tenant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    public static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String tenant = resolveTenant(request);
            TenantContext.setTenantId(tenant);
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }

    private String resolveTenant(HttpServletRequest request) {
        // 1) Header
        String headerTenant = request.getHeader(TENANT_HEADER);
        if (headerTenant != null && !headerTenant.isBlank()) {
            return normalize(headerTenant);
        }
        // 2) Subdomain: {tenant}.yourdomain.com
        String host = request.getHeader("Host");
        if (host != null && host.contains(".")) {
            String sub = host.split(":")[0]; // strip port
            String[] parts = sub.split("\\.");
            if (parts.length >= 3) { // e.g., acme.api.vertexcrm.com
                return normalize(parts[0]);
            }
        }
        return TenantContext.DEFAULT_TENANT;
    }

    private String normalize(String value) {
        return value.trim().toLowerCase().replaceAll("[^a-z0-9_]+", "_");
    }
}
