package com.vertexcrm.Backend.DTO.Response;

import lombok.Data;

@Data
public class MemberResponseDto {
    private Long memberId;
    private String memberName;
    private String memberEmail;

    private String orgName;
    private String roleName;
}
