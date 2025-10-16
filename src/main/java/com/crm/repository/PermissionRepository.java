package com.crm.repository;

import com.crm.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionName(String permissionName);
    List<Permission> findByResource(String resource);
    List<Permission> findByResourceAndAction(String resource, String action);
    List<Permission> findByIsSystem(Boolean isSystem);
}
