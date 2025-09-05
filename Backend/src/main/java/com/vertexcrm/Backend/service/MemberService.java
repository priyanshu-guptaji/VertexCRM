package com.vertexcrm.Backend.service;

import com.vertexcrm.Backend.DTO.Request.MemberRequestDto;
import com.vertexcrm.Backend.DTO.Response.MemberResponseDto;

import java.util.List;

public interface MemberService {
    MemberResponseDto addMember(MemberRequestDto memberDto);
    List<MemberResponseDto> getMembers();
    MemberResponseDto getMemberById(Long id);
    MemberResponseDto updateMember(MemberRequestDto memberDto, Long id);
    MemberResponseDto deleteMember(Long id);
    List<MemberResponseDto> getOrgMember(Long orgId);
    List<MemberResponseDto> getOrgRole(Long orgId, Long roleId);
    MemberResponseDto updateRole(Long memberId, MemberRequestDto memberDto);
}
