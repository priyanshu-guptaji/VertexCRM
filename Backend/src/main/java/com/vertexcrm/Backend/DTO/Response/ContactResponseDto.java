package com.vertexcrm.Backend.DTO.Response;

import lombok.Data;

@Data
public class ContactResponseDto {
    private Long contactId;
    private String contactName;
    private String contactEmail;
    private String contactPhone;

    private String accountName;
    private String orgName;
    private String memberName;
}
