package com.vertexcrm.Backend.service.Impl;

import com.vertexcrm.Backend.DTO.Request.MemberRequestDto;
import com.vertexcrm.Backend.DTO.Response.MemberResponseDto;
import com.vertexcrm.Backend.model.Member;
import com.vertexcrm.Backend.model.Organization;
import com.vertexcrm.Backend.model.Role;
import com.vertexcrm.Backend.repository.LeadRepository;
import com.vertexcrm.Backend.repository.MemberRepository;
import com.vertexcrm.Backend.repository.OrganizationRepository;
import com.vertexcrm.Backend.repository.RoleRepository;
import com.vertexcrm.Backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrganizationRepository orgRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    LeadRepository leadRepository;

    @Override
    public List<MemberResponseDto> getMembers() {
        return memberRepository.findAll().
                stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MemberResponseDto addMember(MemberRequestDto memberDto) {
        Member member = new Member();
        member.setMemberName(memberDto.getMemberName());
        member.setMemberEmail(memberDto.getMemberEmail());
        member.setMemberPassword(memberDto.getMemberPassword());

        Organization org = orgRepository.findById(memberDto.getOrgId())
                .orElseThrow(() -> new RuntimeException("Organization not found" + memberDto.getOrgId()));
        member.setOrg(org);

        Role role = roleRepository.findById(memberDto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found" + memberDto.getRoleId()));
        member.setRole(role);

        Member saved = memberRepository.save(member);
        return mapToResponse(saved);
    }

    @Override
    public MemberResponseDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found" + id));
        return mapToResponse(member);
    }

//    Get member by org id
    @Override
    public List<MemberResponseDto> getOrgMember(Long orgId) {
        List<Member> member = memberRepository.findByOrg_OrgId(orgId);
        return member.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

//    Get members by org and role
    @Override
    public List<MemberResponseDto> getOrgRole(Long orgId, Long roleId) {
        List<Member> member = memberRepository.findByOrg_OrgIdAndRole_RoleId(orgId, roleId);
        return member.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }

//    Update role
    @Override
    public MemberResponseDto updateRole(Long memberId, MemberRequestDto memberDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found " + memberId));
        Role role = roleRepository.findById(memberDto.getRoleId())
                .orElseThrow(() -> new RuntimeException("No roles found " + memberDto));

        member.setRole(role);
        Member updated = memberRepository.save(member);
        return mapToResponse(updated);
    }

    @Override
    public MemberResponseDto updateMember(MemberRequestDto memberDto, Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not Found" + id));

        member.setMemberName(memberDto.getMemberName());
        member.setMemberEmail(memberDto.getMemberEmail());

        Member saved = memberRepository.save(member);

        return mapToResponse(saved);

    }

    @Override
    public MemberResponseDto deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not Found" + id));

        memberRepository.delete(member);
        return mapToResponse(member);
    }

    private MemberResponseDto mapToResponse(Member member) {
        MemberResponseDto response = new MemberResponseDto();
        response.setMemberId(member.getMemberId());
        response.setMemberName(member.getMemberName());
        response.setMemberEmail(member.getMemberEmail());

        if(member.getOrg()!=null) {
            response.setOrgName(member.getOrg().getOrgName());
        }

        if(member.getRole()!=null) {
            response.setRoleName(member.getRole().getRoleName());
        }

        return response;
    }


}
