package com.vertexcrm.Backend.service;

import com.vertexcrm.Backend.DTO.Request.ContactRequestDto;
import com.vertexcrm.Backend.DTO.Response.ContactResponseDto;

import java.util.List;

public interface ContactService {
    List<ContactResponseDto> getContacts();
    ContactResponseDto addContact(ContactRequestDto contactDto);
}
