package com.vertexcrm.Backend.DTO.Request;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class OrganizationRequestDto {
    @Column(nullable = false)
    private String orgName;

    @Column(nullable = false)
    private String orgEmail;
}
