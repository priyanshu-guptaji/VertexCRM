package com.vertexcrm.Backend.DTO.Request;

import lombok.Data;

@Data
public class ContactRequestDto {
    private String contactName;
    private String contactEmail;
    private String contactPhone;

    private Long accountId;
    private Long orgId;
    private Long memberId;

}
