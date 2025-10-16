package com.crm.audit;

import com.crm.entity.AuditLog;
import com.crm.repository.AuditLogRepository;
import com.crm.tenant.TenantContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;

    public AuditAspect(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void anyRestController() {}

    @Pointcut("execution(* *(..)) && (@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping) || @annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public void writeOps() {}

    @AfterReturning(pointcut = "anyRestController() && writeOps()")
    public void afterWrite(JoinPoint jp) {
        String tenant = TenantContext.getTenantId();
        String action = resolveAction(jp);
        String entity = jp.getSignature().getDeclaringType().getSimpleName();
        Long memberId = currentUserId();

        AuditLog log = new AuditLog();
        log.setTenant(tenant);
        log.setAction(action);
        log.setEntityName(entity + "." + jp.getSignature().getName());
        log.setDetails("args=" + Arrays.toString(jp.getArgs()));
        log.setMemberId(memberId);
        // orgId optional: can be set in services where available
        auditLogRepository.save(log);
    }

    private Long currentUserId() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() != null) {
                // If principal carries member id in details or name, adapt here
                return null; // Keep null for now; can be enhanced when user principal is known
            }
        } catch (Exception ignored) {}
        return null;
    }

    private String resolveAction(JoinPoint jp) {
        String name = jp.getSignature().getName().toLowerCase();
        if (name.startsWith("create") || name.startsWith("save") || name.startsWith("post")) return "CREATE";
        if (name.startsWith("update") || name.startsWith("put")) return "UPDATE";
        if (name.startsWith("delete") || name.startsWith("remove")) return "DELETE";
        return "WRITE";
    }
}
