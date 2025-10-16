package com.crm.tenant;

public final class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();
    public static final String DEFAULT_TENANT = "public";

    private TenantContext() {}

    public static void setTenantId(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            CURRENT_TENANT.set(DEFAULT_TENANT);
        } else {
            CURRENT_TENANT.set(tenantId);
        }
    }

    public static String getTenantId() {
        String id = CURRENT_TENANT.get();
        return (id == null || id.isBlank()) ? DEFAULT_TENANT : id;
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
