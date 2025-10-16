package com.crm.controller;

import com.crm.entity.Role;
import com.crm.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
<<<<<<< HEAD
=======
@org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
public class RoleController {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        try {
            List<Role> roles = roleRepository.findAll();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

<<<<<<< HEAD


=======
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80
