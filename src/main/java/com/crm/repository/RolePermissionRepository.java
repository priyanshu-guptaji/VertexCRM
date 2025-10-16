package com.crm.repository;

import com.crm.entity.RolePermission;
import com.crm.entity.Role;
import com.crm.entity.Permission;
import com.crm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByRole(Role role);
    List<RolePermission> findByRoleAndGranted(Role role, Boolean granted);
    List<RolePermission> findByOrganization(Organization organization);
    Optional<RolePermission> findByRoleAndPermission(Role role, Permission permission);
    
    @Query("SELECT rp FROM RolePermission rp LEFT JOIN FETCH rp.permission WHERE rp.role = :role AND rp.granted = true")
    List<RolePermission> findGrantedPermissionsByRole(@Param("role") Role role);
}
