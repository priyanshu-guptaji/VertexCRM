package com.crm.service;

import com.crm.dto.SegmentDto;
import com.crm.entity.Lead;
import com.crm.entity.Organization;
import com.crm.entity.Segment;
import com.crm.repository.LeadRepository;
import com.crm.repository.OrganizationRepository;
import com.crm.repository.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SegmentService {

    @Autowired private SegmentRepository segmentRepository;
    @Autowired private OrganizationRepository organizationRepository;
    @Autowired private LeadRepository leadRepository;

    public SegmentDto create(SegmentDto dto) {
        Organization org = organizationRepository.findById(dto.getOrgId()).orElseThrow();
        Segment s = new Segment();
        s.setOrganization(org);
        s.setName(dto.getName());
        s.setDefinitionJson(dto.getDefinitionJson());
        Segment saved = segmentRepository.save(s);
        return toDto(saved);
    }

    public List<Long> previewLeadIds(Long orgId, Map<String, String> filters) {
        Organization org = organizationRepository.findById(orgId).orElseThrow();
        List<Lead> leads = leadRepository.findByOrganization(org);
        return leads.stream().filter(l -> matches(l, filters)).map(Lead::getLeadId).collect(Collectors.toList());
    }

    private boolean matches(Lead l, Map<String, String> f) {
        if (f == null) return true;
        String emailDomain = f.get("emailDomain");
        if (emailDomain != null && (l.getLeadEmail() == null || !l.getLeadEmail().toLowerCase().endsWith("@" + emailDomain.toLowerCase()))) {
            return false;
        }
        String verified = f.get("isVerified");
        if (verified != null) {
            boolean want = Boolean.parseBoolean(verified);
            if (l.getIsVerified() == null || l.getIsVerified() != want) return false;
        }
        String nameContains = f.get("nameContains");
        if (nameContains != null && (l.getLeadName() == null || !l.getLeadName().toLowerCase().contains(nameContains.toLowerCase()))) {
            return false;
        }
        return true;
    }

    private SegmentDto toDto(Segment s) {
        Long orgId = s.getOrganization() != null ? s.getOrganization().getOrgId() : null;
        return new SegmentDto(s.getSegmentId(), orgId, s.getName(), s.getDefinitionJson(), s.getCreatedAt());
    }
}
