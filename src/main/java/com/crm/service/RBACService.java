package com.crm.service;

import com.crm.entity.*;
import com.crm.repository.*;
import com.crm.dto.PermissionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for managing Role-Based Access Control (RBAC).
 * Handles permissions, roles, and access verification.
 */
@Service
public class RBACService {
    
    @Autowired
    private PermissionRepository permissionRepository;
    
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private OrganizationRepository organizationRepository;
    
    // ========== Permission Management ==========
    
    /**
     * Create a new permission
     */
    @Transactional
    public Permission createPermission(String permissionName, String resource, String action, String description) {
        Permission permission = new Permission();
        permission.setPermissionName(permissionName);
        permission.setResource(resource);
        permission.setAction(action);
        permission.setDescription(description);
        return permissionRepository.save(permission);
    }
    
    /**
     * Get all permissions
     */
    public List<PermissionDto> getAllPermissions() {
        return permissionRepository.findAll().stream()
            .map(this::convertPermissionToDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Get permissions by resource
     */
    public List<PermissionDto> getPermissionsByResource(String resource) {
        return permissionRepository.findByResource(resource).stream()
            .map(this::convertPermissionToDto)
            .collect(Collectors.toList());
    }
    
    // ========== Role Permission Management ==========
    
    /**
     * Assign permission to role
     */
    @Transactional
    public RolePermission assignPermissionToRole(Long roleId, Long permissionId, Long orgId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role not found"));
        Permission permission = permissionRepository.findById(permissionId)
            .orElseThrow(() -> new RuntimeException("Permission not found"));
        Organization organization = organizationRepository.findById(orgId)
            .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        // Check if already exists
        Optional<RolePermission> existing = rolePermissionRepository.findByRoleAndPermission(role, permission);
        if (existing.isPresent()) {
            return existing.get();
        }
        
        RolePermission rolePermission = new RolePermission(role, permission, organization);
        return rolePermissionRepository.save(rolePermission);
    }
    
    /**
     * Remove permission from role
     */
    @Transactional
    public void removePermissionFromRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role not found"));
        Permission permission = permissionRepository.findById(permissionId)
            .orElseThrow(() -> new RuntimeException("Permission not found"));
        
        Optional<RolePermission> rolePermission = rolePermissionRepository.findByRoleAndPermission(role, permission);
        rolePermission.ifPresent(rp -> rolePermissionRepository.delete(rp));
    }
    
    /**
     * Get all permissions for a role
     */
    public List<PermissionDto> getPermissionsByRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role not found"));
        
        List<RolePermission> rolePermissions = rolePermissionRepository.findGrantedPermissionsByRole(role);
        return rolePermissions.stream()
            .map(rp -> convertPermissionToDto(rp.getPermission()))
            .collect(Collectors.toList());
    }
    
    // ========== Access Control Methods ==========
    
    /**
     * Check if a member has a specific permission
     */
    public boolean hasPermission(Long memberId, String resource, String action) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            return false;
        }
        
        Role role = member.getRole();
        if (role == null) {
            return false;
        }
        
        // Get all granted permissions for the role
        List<RolePermission> rolePermissions = rolePermissionRepository.findGrantedPermissionsByRole(role);
        
        // Check if any permission matches
        return rolePermissions.stream()
            .anyMatch(rp -> {
                Permission p = rp.getPermission();
                return p.getResource().equals(resource) && p.getAction().equals(action);
            });
    }
    
    /**
     * Check if a member has permission by permission name
     */
    public boolean hasPermissionByName(Long memberId, String permissionName) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            return false;
        }
        
        Role role = member.getRole();
        if (role == null) {
            return false;
        }
        
        List<RolePermission> rolePermissions = rolePermissionRepository.findGrantedPermissionsByRole(role);
        
        return rolePermissions.stream()
            .anyMatch(rp -> rp.getPermission().getPermissionName().equals(permissionName));
    }
    
    /**
     * Get all permissions for a member
     */
    public List<String> getMemberPermissions(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            return new ArrayList<>();
        }
        
        Role role = member.getRole();
        if (role == null) {
            return new ArrayList<>();
        }
        
        List<RolePermission> rolePermissions = rolePermissionRepository.findGrantedPermissionsByRole(role);
        
        return rolePermissions.stream()
            .map(rp -> rp.getPermission().getPermissionName())
            .collect(Collectors.toList());
    }
    
    // ========== Initialization Methods ==========
    
    /**
     * Initialize default permissions for the system
     */
    @Transactional
    public void initializeDefaultPermissions() {
        // Lead permissions
        createDefaultPermission("LEAD_CREATE", "LEAD", "CREATE", "Create new leads");
        createDefaultPermission("LEAD_READ", "LEAD", "READ", "View leads");
        createDefaultPermission("LEAD_UPDATE", "LEAD", "UPDATE", "Update lead information");
        createDefaultPermission("LEAD_DELETE", "LEAD", "DELETE", "Delete leads");
        
        // Contact permissions
        createDefaultPermission("CONTACT_CREATE", "CONTACT", "CREATE", "Create new contacts");
        createDefaultPermission("CONTACT_READ", "CONTACT", "READ", "View contacts");
        createDefaultPermission("CONTACT_UPDATE", "CONTACT", "UPDATE", "Update contact information");
        createDefaultPermission("CONTACT_DELETE", "CONTACT", "DELETE", "Delete contacts");
        
        // Deal permissions
        createDefaultPermission("DEAL_CREATE", "DEAL", "CREATE", "Create new deals");
        createDefaultPermission("DEAL_READ", "DEAL", "READ", "View deals");
        createDefaultPermission("DEAL_UPDATE", "DEAL", "UPDATE", "Update deal information");
        createDefaultPermission("DEAL_DELETE", "DEAL", "DELETE", "Delete deals");
        
        // Campaign permissions
        createDefaultPermission("CAMPAIGN_CREATE", "CAMPAIGN", "CREATE", "Create new campaigns");
        createDefaultPermission("CAMPAIGN_READ", "CAMPAIGN", "READ", "View campaigns");
        createDefaultPermission("CAMPAIGN_UPDATE", "CAMPAIGN", "UPDATE", "Update campaigns");
        createDefaultPermission("CAMPAIGN_DELETE", "CAMPAIGN", "DELETE", "Delete campaigns");
        
        // Ticket permissions
        createDefaultPermission("TICKET_CREATE", "TICKET", "CREATE", "Create new tickets");
        createDefaultPermission("TICKET_READ", "TICKET", "READ", "View tickets");
        createDefaultPermission("TICKET_UPDATE", "TICKET", "UPDATE", "Update tickets");
        createDefaultPermission("TICKET_DELETE", "TICKET", "DELETE", "Delete tickets");
        
        // Automation permissions
        createDefaultPermission("AUTOMATION_MANAGE", "AUTOMATION", "MANAGE", "Manage automation rules");
        createDefaultPermission("ANALYTICS_VIEW", "ANALYTICS", "READ", "View analytics and reports");
        
        // Admin permissions
        createDefaultPermission("USER_MANAGE", "USER", "MANAGE", "Manage users and roles");
        createDefaultPermission("SETTINGS_MANAGE", "SETTINGS", "MANAGE", "Manage system settings");
    }
    
    private void createDefaultPermission(String name, String resource, String action, String description) {
        Optional<Permission> existing = permissionRepository.findByPermissionName(name);
        if (existing.isEmpty()) {
            Permission permission = new Permission(name, resource, action);
            permission.setDescription(description);
            permission.setIsSystem(true);
            permissionRepository.save(permission);
        }
    }
    
    // ========== Helper Methods ==========
    
    private PermissionDto convertPermissionToDto(Permission permission) {
        return new PermissionDto(
            permission.getPermissionId(),
            permission.getPermissionName(),
            permission.getResource(),
            permission.getAction(),
            permission.getDescription(),
            permission.getIsSystem()
        );
    }
}
