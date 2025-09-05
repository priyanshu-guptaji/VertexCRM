package com.vertexcrm.Backend.DTO.Request;

import lombok.Data;

@Data
public class MemberRequestDto {
    private String memberName;
    private String memberEmail;
    private String memberPassword;

    private Long orgId;
    private Long RoleId;
}
