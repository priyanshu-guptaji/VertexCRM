package com.crm.controller;

import com.crm.dto.PermissionDto;
import com.crm.entity.Permission;
import com.crm.entity.RolePermission;
import com.crm.service.RBACService;
import com.crm.util.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Role-Based Access Control management.
 * Provides endpoints for permission and role management.
 */
@RestController
@RequestMapping("/api/rbac")
@CrossOrigin
public class RBACController {
    
    @Autowired
    private RBACService rbacService;
    
    @Autowired
    private AuthenticationUtils authenticationUtils;
    
    // ========== Permission Endpoints ==========
    
    /**
     * Get all available permissions
     */
    @GetMapping("/permissions")
    public ResponseEntity<List<PermissionDto>> getAllPermissions() {
        List<PermissionDto> permissions = rbacService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }
    
    /**
     * Get permissions by resource
     */
    @GetMapping("/permissions/resource/{resource}")
    public ResponseEntity<List<PermissionDto>> getPermissionsByResource(@PathVariable String resource) {
        List<PermissionDto> permissions = rbacService.getPermissionsByResource(resource);
        return ResponseEntity.ok(permissions);
    }
    
    /**
     * Create a new permission
     */
    @PostMapping("/permissions")
    public ResponseEntity<Permission> createPermission(@RequestBody Map<String, String> request) {
        String permissionName = request.get("permissionName");
        String resource = request.get("resource");
        String action = request.get("action");
        String description = request.get("description");
        
        Permission permission = rbacService.createPermission(permissionName, resource, action, description);
        return ResponseEntity.ok(permission);
    }
    
    /**
     * Initialize default system permissions
     */
    @PostMapping("/permissions/initialize")
    public ResponseEntity<Map<String, String>> initializePermissions() {
        rbacService.initializeDefaultPermissions();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Default permissions initialized successfully");
        return ResponseEntity.ok(response);
    }
    
    // ========== Role Permission Endpoints ==========
    
    /**
     * Get all permissions for a specific role
     */
    @GetMapping("/roles/{roleId}/permissions")
    public ResponseEntity<List<PermissionDto>> getRolePermissions(@PathVariable Long roleId) {
        List<PermissionDto> permissions = rbacService.getPermissionsByRole(roleId);
        return ResponseEntity.ok(permissions);
    }
    
    /**
     * Assign permission to role
     */
    @PostMapping("/roles/{roleId}/permissions")
    public ResponseEntity<RolePermission> assignPermissionToRole(
            @PathVariable Long roleId,
            @RequestBody Map<String, Long> request) {
        Long permissionId = request.get("permissionId");
        Long orgId = authenticationUtils.getCurrentOrgId();
        
        RolePermission rolePermission = rbacService.assignPermissionToRole(roleId, permissionId, orgId);
        return ResponseEntity.ok(rolePermission);
    }
    
    /**
     * Remove permission from role
     */
    @DeleteMapping("/roles/{roleId}/permissions/{permissionId}")
    public ResponseEntity<Map<String, String>> removePermissionFromRole(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {
        rbacService.removePermissionFromRole(roleId, permissionId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Permission removed from role successfully");
        return ResponseEntity.ok(response);
    }
    
    // ========== Member Permission Endpoints ==========
    
    /**
     * Get all permissions for the current member
     */
    @GetMapping("/members/me/permissions")
    public ResponseEntity<List<String>> getMyPermissions() {
        Long memberId = authenticationUtils.getCurrentMemberId();
        List<String> permissions = rbacService.getMemberPermissions(memberId);
        return ResponseEntity.ok(permissions);
    }
    
    /**
     * Get permissions for a specific member
     */
    @GetMapping("/members/{memberId}/permissions")
    public ResponseEntity<List<String>> getMemberPermissions(@PathVariable Long memberId) {
        List<String> permissions = rbacService.getMemberPermissions(memberId);
        return ResponseEntity.ok(permissions);
    }
    
    /**
     * Check if current member has a specific permission
     */
    @PostMapping("/check-permission")
    public ResponseEntity<Map<String, Boolean>> checkPermission(@RequestBody Map<String, String> request) {
        Long memberId = authenticationUtils.getCurrentMemberId();
        String resource = request.get("resource");
        String action = request.get("action");
        
        boolean hasPermission = rbacService.hasPermission(memberId, resource, action);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasPermission", hasPermission);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Check if current member has permission by name
     */
    @PostMapping("/check-permission-by-name")
    public ResponseEntity<Map<String, Boolean>> checkPermissionByName(@RequestBody Map<String, String> request) {
        Long memberId = authenticationUtils.getCurrentMemberId();
        String permissionName = request.get("permissionName");
        
        boolean hasPermission = rbacService.hasPermissionByName(memberId, permissionName);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasPermission", hasPermission);
        return ResponseEntity.ok(response);
    }
}
