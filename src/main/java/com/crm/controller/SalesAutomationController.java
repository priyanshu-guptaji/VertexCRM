package com.crm.controller;

import com.crm.dto.LeadScoreDto;
import com.crm.dto.PipelineDto;
import com.crm.entity.*;
import com.crm.repository.*;
import com.crm.service.SalesAutomationService;
import com.crm.util.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Sales Automation features.
 * Provides endpoints for lead scoring, pipelines, and automation rules.
 */
@RestController
@RequestMapping("/api/sales")
@CrossOrigin
public class SalesAutomationController {
    
    @Autowired
    private SalesAutomationService salesAutomationService;
    
    @Autowired
    private LeadScoreRepository leadScoreRepository;
    
    @Autowired
    private PipelineRepository pipelineRepository;
    
    @Autowired
    private DealStageRuleRepository dealStageRuleRepository;
    
    @Autowired
    private FollowUpRuleRepository followUpRuleRepository;
    
    @Autowired
    private OrganizationRepository organizationRepository;
    
    @Autowired
    private LeadRepository leadRepository;
    
    @Autowired
    private AuthenticationUtils authenticationUtils;
    
    // ========== Lead Scoring Endpoints ==========
    
    /**
     * Get all lead scores for current organization
     */
    @GetMapping("/lead-scores")
    public ResponseEntity<List<LeadScoreDto>> getAllLeadScores() {
        Long orgId = authenticationUtils.getCurrentOrgId();
        Organization org = organizationRepository.findById(orgId)
            .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        List<LeadScoreDto> scores = salesAutomationService.getAllLeadScores(org);
        return ResponseEntity.ok(scores);
    }
    
    /**
     * Get top-scoring leads
     */
    @GetMapping("/top-leads")
    public ResponseEntity<List<LeadScoreDto>> getTopLeads(@RequestParam(defaultValue = "60") Integer minScore) {
        Long orgId = authenticationUtils.getCurrentOrgId();
        Organization org = organizationRepository.findById(orgId)
            .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        List<LeadScoreDto> topLeads = salesAutomationService.getTopLeads(org, minScore);
        return ResponseEntity.ok(topLeads);
    }
    
    /**
     * Update lead score based on activity
     */
    @PostMapping("/lead-scores/activity")
    public ResponseEntity<Map<String, String>> updateLeadScoreOnActivity(@RequestBody Map<String, Object> request) {
        Long leadId = Long.parseLong(request.get("leadId").toString());
        String activityType = request.get("activityType").toString();
        
        Lead lead = leadRepository.findById(leadId)
            .orElseThrow(() -> new RuntimeException("Lead not found"));
        
        salesAutomationService.updateLeadScoreOnActivity(lead, activityType);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Lead score updated successfully");
        return ResponseEntity.ok(response);
    }
    
    // ========== Pipeline Endpoints ==========
    
    /**
     * Get all pipelines
     */
    @GetMapping("/pipelines")
    public ResponseEntity<List<Pipeline>> getAllPipelines() {
        Long orgId = authenticationUtils.getCurrentOrgId();
        Organization org = organizationRepository.findById(orgId)
            .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        List<Pipeline> pipelines = pipelineRepository.findByOrganization(org);
        return ResponseEntity.ok(pipelines);
    }
    
    /**
     * Get default pipeline
     */
    @GetMapping("/pipelines/default")
    public ResponseEntity<Pipeline> getDefaultPipeline() {
        Long orgId = authenticationUtils.getCurrentOrgId();
        Organization org = organizationRepository.findById(orgId)
            .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        Pipeline pipeline = pipelineRepository.findByOrganizationAndIsDefault(org, true)
            .orElseThrow(() -> new RuntimeException("No default pipeline found"));
        
        return ResponseEntity.ok(pipeline);
    }
    
    /**
     * Create a new pipeline
     */
    @PostMapping("/pipelines")
    public ResponseEntity<Pipeline> createPipeline(@RequestBody PipelineDto pipelineDto) {
        Long orgId = authenticationUtils.getCurrentOrgId();
        Organization org = organizationRepository.findById(orgId)
            .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        Pipeline pipeline = new Pipeline();
        pipeline.setPipelineName(pipelineDto.getPipelineName());
        pipeline.setDescription(pipelineDto.getDescription());
        pipeline.setStages(pipelineDto.getStages());
        pipeline.setIsDefault(pipelineDto.getIsDefault());
        pipeline.setIsActive(true);
        pipeline.setOrganization(org);
        
        Pipeline saved = pipelineRepository.save(pipeline);
        return ResponseEntity.ok(saved);
    }
    
    /**
     * Update pipeline
     */
    @PutMapping("/pipelines/{id}")
    public ResponseEntity<Pipeline> updatePipeline(@PathVariable Long id, @RequestBody PipelineDto pipelineDto) {
        Pipeline pipeline = pipelineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pipeline not found"));
        
        pipeline.setPipelineName(pipelineDto.getPipelineName());
        pipeline.setDescription(pipelineDto.getDescription());
        pipeline.setStages(pipelineDto.getStages());
        pipeline.setIsDefault(pipelineDto.getIsDefault());
        
        Pipeline updated = pipelineRepository.save(pipeline);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Delete pipeline
     */
    @DeleteMapping("/pipelines/{id}")
    public ResponseEntity<Map<String, String>> deletePipeline(@PathVariable Long id) {
        pipelineRepository.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Pipeline deleted successfully");
        return ResponseEntity.ok(response);
    }
    
    // ========== Deal Stage Rule Endpoints ==========
    
    /**
     * Get all deal stage rules
     */
    @GetMapping("/stage-rules")
    public ResponseEntity<List<DealStageRule>> getAllStageRules() {
        Long orgId = authenticationUtils.getCurrentOrgId();
        Organization org = organizationRepository.findById(orgId)
            .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        List<DealStageRule> rules = dealStageRuleRepository.findByOrganization(org);
        return ResponseEntity.ok(rules);
    }
    
    /**
     * Create deal stage rule
     */
    @PostMapping("/stage-rules")
    public ResponseEntity<DealStageRule> createStageRule(@RequestBody DealStageRule rule) {
        Long orgId = authenticationUtils.getCurrentOrgId();
        Organization org = organizationRepository.findById(orgId)
            .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        rule.setOrganization(org);
        DealStageRule saved = dealStageRuleRepository.save(rule);
        return ResponseEntity.ok(saved);
    }
    
    /**
     * Update deal stage rule
     */
    @PutMapping("/stage-rules/{id}")
    public ResponseEntity<DealStageRule> updateStageRule(@PathVariable Long id, @RequestBody DealStageRule rule) {
        DealStageRule existing = dealStageRuleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rule not found"));
        
        existing.setRuleName(rule.getRuleName());
        existing.setDescription(rule.getDescription());
        existing.setSourceStage(rule.getSourceStage());
        existing.setTargetStage(rule.getTargetStage());
        existing.setTriggerType(rule.getTriggerType());
        existing.setIsActive(rule.getIsActive());
        existing.setPriority(rule.getPriority());
        
        DealStageRule updated = dealStageRuleRepository.save(existing);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Delete deal stage rule
     */
    @DeleteMapping("/stage-rules/{id}")
    public ResponseEntity<Map<String, String>> deleteStageRule(@PathVariable Long id) {
        dealStageRuleRepository.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Stage rule deleted successfully");
        return ResponseEntity.ok(response);
    }
    
    // ========== Follow-up Rule Endpoints ==========
    
    /**
     * Get all follow-up rules
     */
    @GetMapping("/follow-up-rules")
    public ResponseEntity<List<FollowUpRule>> getAllFollowUpRules() {
        Long orgId = authenticationUtils.getCurrentOrgId();
        Organization org = organizationRepository.findById(orgId)
            .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        List<FollowUpRule> rules = followUpRuleRepository.findByOrganization(org);
        return ResponseEntity.ok(rules);
    }
    
    /**
     * Create follow-up rule
     */
    @PostMapping("/follow-up-rules")
    public ResponseEntity<FollowUpRule> createFollowUpRule(@RequestBody FollowUpRule rule) {
        Long orgId = authenticationUtils.getCurrentOrgId();
        Organization org = organizationRepository.findById(orgId)
            .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        rule.setOrganization(org);
        FollowUpRule saved = followUpRuleRepository.save(rule);
        return ResponseEntity.ok(saved);
    }
    
    /**
     * Update follow-up rule
     */
    @PutMapping("/follow-up-rules/{id}")
    public ResponseEntity<FollowUpRule> updateFollowUpRule(@PathVariable Long id, @RequestBody FollowUpRule rule) {
        FollowUpRule existing = followUpRuleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rule not found"));
        
        existing.setRuleName(rule.getRuleName());
        existing.setDescription(rule.getDescription());
        existing.setEntityType(rule.getEntityType());
        existing.setInactivityDays(rule.getInactivityDays());
        existing.setActionType(rule.getActionType());
        existing.setTaskTitle(rule.getTaskTitle());
        existing.setTaskDescription(rule.getTaskDescription());
        existing.setNotificationMessage(rule.getNotificationMessage());
        existing.setIsActive(rule.getIsActive());
        
        FollowUpRule updated = followUpRuleRepository.save(existing);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Delete follow-up rule
     */
    @DeleteMapping("/follow-up-rules/{id}")
    public ResponseEntity<Map<String, String>> deleteFollowUpRule(@PathVariable Long id) {
        followUpRuleRepository.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Follow-up rule deleted successfully");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Trigger follow-up checks manually
     */
    @PostMapping("/follow-up-rules/trigger")
    public ResponseEntity<Map<String, String>> triggerFollowUpChecks() {
        Long orgId = authenticationUtils.getCurrentOrgId();
        Organization org = organizationRepository.findById(orgId)
            .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        salesAutomationService.checkAndCreateFollowUps(org);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Follow-up checks triggered successfully");
        return ResponseEntity.ok(response);
    }
}
