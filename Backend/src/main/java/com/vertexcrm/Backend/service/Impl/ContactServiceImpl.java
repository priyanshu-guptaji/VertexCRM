package com.vertexcrm.Backend.service.Impl;

import com.vertexcrm.Backend.DTO.Request.ContactRequestDto;
import com.vertexcrm.Backend.DTO.Response.ContactResponseDto;
import com.vertexcrm.Backend.model.Account;
import com.vertexcrm.Backend.model.Contact;
import com.vertexcrm.Backend.model.Member;
import com.vertexcrm.Backend.model.Organization;
import com.vertexcrm.Backend.repository.AccountRepository;
import com.vertexcrm.Backend.repository.ContactRepository;
import com.vertexcrm.Backend.repository.MemberRepository;
import com.vertexcrm.Backend.repository.OrganizationRepository;
import com.vertexcrm.Backend.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private OrganizationRepository orgRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Override
    public List<ContactResponseDto> getContacts() {
        List<Contact> contacts = contactRepo.findAll();
        return contacts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ContactResponseDto addContact(ContactRequestDto contactDto) {
        Contact contact = new Contact();
        contact.setContactName(contactDto.getContactName());
        contact.setContactEmail(contactDto.getContactEmail()); // ✅ fixed
        contact.setContactPhone(contactDto.getContactPhone()); // ✅ fixed

        // set Account
        Account account = accountRepo.findById(contactDto.getAccountId())
                .orElseThrow(() -> new RuntimeException("No Account found with ID: " + contactDto.getAccountId()));
        contact.setAccount(account);

        // set Organization if provided
        if (contactDto.getOrgId() != null) {
            Organization org = orgRepo.findById(contactDto.getOrgId())
                    .orElseThrow(() -> new RuntimeException("No Organization found with ID: " + contactDto.getOrgId()));
            contact.setOrg(org);
        }

        // set Member if provided
        if (contactDto.getMemberId() != null) {
            Member member = memberRepo.findById(contactDto.getMemberId())
                    .orElseThrow(() -> new RuntimeException("No Member found with ID: " + contactDto.getMemberId()));
            contact.setMember(member);
        }

        // Save contact
        Contact savedContact = contactRepo.save(contact);

        // Return mapped response
        return mapToResponse(savedContact);
    }

    private ContactResponseDto mapToResponse(Contact contact) {
        ContactResponseDto response = new ContactResponseDto();
        response.setContactId(contact.getContactId());
        response.setContactName(contact.getContactName());
        response.setContactEmail(contact.getContactEmail());
        response.setContactPhone(contact.getContactPhone());

        if (contact.getAccount() != null) {
            response.setAccountName(contact.getAccount().getAccountName());
        }

        if (contact.getOrg() != null) {
            response.setOrgName(contact.getOrg().getOrgName());
        }

        if (contact.getMember() != null) {
            response.setMemberName(contact.getMember().getMemberName());
        }
        return response;
    }
}
