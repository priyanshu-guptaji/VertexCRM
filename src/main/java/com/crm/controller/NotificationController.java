package com.crm.controller;

import com.crm.dto.NotificationDto;
import com.crm.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired private NotificationService notificationService;

    @GetMapping("/org/{orgId}/member/{memberId}")
    public ResponseEntity<?> list(@PathVariable Long orgId, @PathVariable Long memberId) {
        try {
            return ResponseEntity.ok(notificationService.list(orgId, memberId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
