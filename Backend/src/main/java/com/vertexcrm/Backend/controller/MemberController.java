package com.vertexcrm.Backend.controller;

import com.vertexcrm.Backend.DTO.Request.MemberRequestDto;
import com.vertexcrm.Backend.DTO.Response.MemberResponseDto;
import com.vertexcrm.Backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/member")
    public ResponseEntity<List<MemberResponseDto>> getMembers() {
        return new ResponseEntity<>(memberService.getMembers(), HttpStatus.OK);
    }
//  Add member
    @PostMapping("/member")
    public ResponseEntity<MemberResponseDto> addMember(@RequestBody MemberRequestDto memberDto) {
        return new ResponseEntity<>(memberService.addMember(memberDto), HttpStatus.OK);
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<MemberResponseDto> getMemberById(@PathVariable Long id) {
        MemberResponseDto member = memberService.getMemberById(id);
        if(member != null) {
            return new ResponseEntity<>(member, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    Get by Org
    @GetMapping("/org/{orgId}/member")
    public ResponseEntity<List<MemberResponseDto>> getOrgMember(@PathVariable Long orgId) {
        List<MemberResponseDto> member = memberService.getOrgMember(orgId);

        if(member != null) {
            return new ResponseEntity<>(member, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    Get by org and role
    @GetMapping("/org/{orgId}/role/{roleId}/member")
    public ResponseEntity<List<MemberResponseDto>> getOrgRole(@PathVariable Long orgId,
                                                              @PathVariable Long roleId) {
        List<MemberResponseDto> member = memberService.getOrgRole(orgId, roleId);
        if(member != null) {
            return new ResponseEntity<>(member, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    Update RoleName
    @PutMapping("member/{memberId}/role")
    public ResponseEntity<MemberResponseDto> updateRole(@PathVariable Long memberId,
                                                        @RequestBody MemberRequestDto memberDto) {
        MemberResponseDto member = memberService.updateRole(memberId, memberDto);

        if(member != null) {
            return new ResponseEntity<>(member, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //    Update member
    @PutMapping("/member/{id}")
    public ResponseEntity<MemberResponseDto> updateMember(@RequestBody MemberRequestDto memberDto,
                                                          @PathVariable Long id) {
        MemberResponseDto member = memberService.updateMember(memberDto, id);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    //    Delete member
    @DeleteMapping("/member/{id}")
    public ResponseEntity<MemberResponseDto> deleteMember(@PathVariable Long id) {
        MemberResponseDto member = memberService.deleteMember(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
