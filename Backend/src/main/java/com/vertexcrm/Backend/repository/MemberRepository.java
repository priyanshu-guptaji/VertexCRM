package com.vertexcrm.Backend.repository;

import com.vertexcrm.Backend.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByOrg_OrgId(Long orgId);
    List<Member> findByOrg_OrgIdAndRole_RoleId(Long orgId, Long roleId);
}
