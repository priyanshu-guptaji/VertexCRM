package com.vertexcrm.Backend.controller;

import com.vertexcrm.Backend.DTO.Request.ContactRequestDto;
import com.vertexcrm.Backend.DTO.Response.ContactResponseDto;
import com.vertexcrm.Backend.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ContactController {

    @Autowired
    ContactService contactService;

    @GetMapping("/contacts")
    public ResponseEntity<List<ContactResponseDto>> getContacts() {
        return new ResponseEntity<>(contactService.getContacts(), HttpStatus.OK);
    }

    @PostMapping("/org/contact")
    public ResponseEntity<ContactResponseDto> addContact(ContactRequestDto contactDto) {
        return new ResponseEntity<>(contactService.addContact(contactDto), HttpStatus.OK);
    }
}
