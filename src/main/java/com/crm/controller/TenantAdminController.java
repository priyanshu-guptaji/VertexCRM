package com.crm.controller;

import com.crm.service.TenantSchemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/tenants")
@CrossOrigin(origins = "*")
public class TenantAdminController {

    private final TenantSchemaService tenantSchemaService;

    public TenantAdminController(TenantSchemaService tenantSchemaService) {
        this.tenantSchemaService = tenantSchemaService;
    }

    @PostMapping("/{schema}/provision")
    public ResponseEntity<?> provision(@PathVariable String schema) {
        try {
            tenantSchemaService.provisionSchema(schema);
            return ResponseEntity.ok("Schema provisioned: " + schema);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
