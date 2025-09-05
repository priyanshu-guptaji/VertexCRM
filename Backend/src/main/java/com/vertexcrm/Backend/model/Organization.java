package com.vertexcrm.Backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "organization")
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orgId;

    private String orgName;
    private String orgEmail;

    @OneToMany(mappedBy = "org")
    private List<Member> members;

    @OneToMany(mappedBy = "org", cascade = CascadeType.ALL)
    private List<Lead> leads;

    @OneToMany(mappedBy = "org")
    private List<Contact> contacts;

    @OneToMany(mappedBy = "org")
    private List<Activity> activities;

    @OneToMany(mappedBy = "org")
    private List<Note> notes;
}
