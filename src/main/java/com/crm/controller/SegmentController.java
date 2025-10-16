package com.crm.controller;

import com.crm.dto.SegmentDto;
import com.crm.service.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/segments")
@CrossOrigin(origins = "*")
public class SegmentController {

    @Autowired private SegmentService segmentService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SegmentDto dto) {
        try { return ResponseEntity.ok(segmentService.create(dto)); }
        catch (Exception e) { return ResponseEntity.badRequest().body("Error: " + e.getMessage()); }
    }

    @PostMapping("/org/{orgId}/preview")
    public ResponseEntity<?> preview(@PathVariable Long orgId, @RequestBody Map<String, String> filters) {
        try { return ResponseEntity.ok(segmentService.previewLeadIds(orgId, filters)); }
        catch (Exception e) { return ResponseEntity.badRequest().body("Error: " + e.getMessage()); }
    }
}
