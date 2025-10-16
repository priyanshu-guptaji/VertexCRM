package com.crm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/integrations/webhooks")
@CrossOrigin(origins = "*")
public class IntegrationWebhookController {

    private static final Logger log = LoggerFactory.getLogger(IntegrationWebhookController.class);

    @PostMapping("/email-events")
    public ResponseEntity<?> emailEvents(@RequestBody Map<String, Object> payload) {
        log.info("Email event webhook: {}", payload);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/zapier")
    public ResponseEntity<?> zapier(@RequestBody Map<String, Object> payload) {
        log.info("Zapier webhook: {}", payload);
        return ResponseEntity.ok("ok");
    }
}
