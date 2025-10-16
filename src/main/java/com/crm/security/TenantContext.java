package com.crm.security;

public class TenantContext {

    private static final ThreadLocal<Long> ORG_ID_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<Long> MEMBER_ID_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<String> ROLE_HOLDER = new ThreadLocal<>();

    public static void setOrgId(Long orgId) {
        ORG_ID_HOLDER.set(orgId);
    }

    public static Long getOrgId() {
        return ORG_ID_HOLDER.get();
    }

    public static void setMemberId(Long memberId) {
        MEMBER_ID_HOLDER.set(memberId);
    }

    public static Long getMemberId() {
        return MEMBER_ID_HOLDER.get();
    }

    public static void setRole(String role) {
        ROLE_HOLDER.set(role);
    }

    public static String getRole() {
        return ROLE_HOLDER.get();
    }

    public static void clear() {
        ORG_ID_HOLDER.remove();
        MEMBER_ID_HOLDER.remove();
        ROLE_HOLDER.remove();
    }
}


