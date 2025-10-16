package com.crm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PermissionDto {
    
    private Long permissionId;
    
    @NotBlank(message = "Permission name is required")
    @Size(max = 100)
    private String permissionName;
    
    @NotBlank(message = "Resource is required")
    @Size(max = 50)
    private String resource;
    
    @NotBlank(message = "Action is required")
    @Size(max = 50)
    private String action;
    
    private String description;
    private Boolean isSystem;
    
    // Constructors
    public PermissionDto() {}
    
    public PermissionDto(Long permissionId, String permissionName, String resource, String action, String description, Boolean isSystem) {
        this.permissionId = permissionId;
        this.permissionName = permissionName;
        this.resource = resource;
        this.action = action;
        this.description = description;
        this.isSystem = isSystem;
    }
    
    // Getters and Setters
    public Long getPermissionId() {
        return permissionId;
    }
    
    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }
    
    public String getPermissionName() {
        return permissionName;
    }
    
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
    
    public String getResource() {
        return resource;
    }
    
    public void setResource(String resource) {
        this.resource = resource;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getIsSystem() {
        return isSystem;
    }
    
    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }
}
