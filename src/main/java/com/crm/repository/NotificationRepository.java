package com.crm.repository;

import com.crm.entity.Notification;
import com.crm.entity.Organization;
import com.crm.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByOrganizationAndRecipientOrderByCreatedAtDesc(Organization organization, Member recipient);
}
