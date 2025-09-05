package com.vertexcrm.Backend.DTO.Request;

import lombok.Data;

@Data
public class LeadRequestDto {
    private String leadName;
    private String leadEmail;
    private boolean isVerified;
    private String phone;

    private Long orgId;
    private Long memberId;
}
