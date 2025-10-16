package com.crm.controller;

import com.crm.dto.KnowledgeBaseArticleDto;
import com.crm.service.KnowledgeBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kb")
@CrossOrigin(origins = "*")
public class KnowledgeBaseController {

    @Autowired
    private KnowledgeBaseService kbService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody KnowledgeBaseArticleDto dto) {
        try {
            return ResponseEntity.ok(kbService.create(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody KnowledgeBaseArticleDto dto) {
        try {
            return ResponseEntity.ok(kbService.update(id, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(kbService.get(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/org/{orgId}")
    public ResponseEntity<?> listByOrg(@PathVariable Long orgId) {
        try {
            List<KnowledgeBaseArticleDto> list = kbService.listByOrg(orgId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            kbService.delete(id);
            return ResponseEntity.ok("Deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
