package com.vertexcrm.Backend.DTO.Response;

import lombok.Data;

@Data
public class OrganizationResponseDto {
    private Long orgId;
    private String orgName;
    private String orgEmail;
}
