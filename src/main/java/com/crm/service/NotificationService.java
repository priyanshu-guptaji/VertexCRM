package com.crm.service;

import com.crm.dto.NotificationDto;
import com.crm.entity.Member;
import com.crm.entity.Notification;
import com.crm.entity.Organization;
import com.crm.repository.MemberRepository;
import com.crm.repository.NotificationRepository;
import com.crm.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired private NotificationRepository notificationRepository;
    @Autowired private OrganizationRepository organizationRepository;
    @Autowired private MemberRepository memberRepository;

    @Async
    public void send(Long orgId, Long memberId, String type, String title, String message) {
        Organization org = organizationRepository.findById(orgId).orElseThrow();
        Member member = memberRepository.findById(memberId).orElse(null);
        Notification n = new Notification();
        n.setOrganization(org);
        n.setRecipient(member);
        n.setType(type);
        n.setTitle(title);
        n.setMessage(message);
        notificationRepository.save(n);
    }

    public List<NotificationDto> list(Long orgId, Long memberId) {
        Organization org = organizationRepository.findById(orgId).orElseThrow();
        Member member = memberRepository.findById(memberId).orElseThrow();
        return notificationRepository.findByOrganizationAndRecipientOrderByCreatedAtDesc(org, member)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    private NotificationDto toDto(Notification n) {
        Long orgId = n.getOrganization() != null ? n.getOrganization().getOrgId() : null;
        Long memberId = n.getRecipient() != null ? n.getRecipient().getMemberId() : null;
        return new NotificationDto(
                n.getNotificationId(), orgId, memberId, n.getType(), n.getTitle(), n.getMessage(), n.getRead(), n.getCreatedAt()
        );
    }
}
