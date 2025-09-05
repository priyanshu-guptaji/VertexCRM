package com.vertexcrm.Backend.DTO.Response;

import lombok.Data;

@Data
public class LeadResponseDto {
    private Long leadId;
    private String leadName;
    private String leadEmail;
    private boolean isVerified;
    private String phone;

    private String orgName;
    private String memberName;
}
