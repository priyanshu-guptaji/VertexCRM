package com.crm.service;

import com.crm.dto.SupportRequestDto;
import com.crm.entity.Member;
import com.crm.entity.Organization;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String defaultFrom;

    @Value("${app.support.to-email:}")
    private String supportToEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a support request email to the configured support address.
     */
    public void sendSupportRequest(SupportRequestDto request,
                                   Member member,
                                   Organization organization) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            String fromAddress = defaultFrom != null && !defaultFrom.isBlank()
                    ? defaultFrom
                    : "no-reply@localhost";

            String toAddress = (supportToEmail != null && !supportToEmail.isBlank())
                    ? supportToEmail
                    : fromAddress;

            helper.setFrom(new InternetAddress(fromAddress, organization != null
                    ? organization.getOrgName()
                    : "CRM"));
            helper.setTo(toAddress);

            // Use user's email as Reply-To so support can respond directly
            String replyTo = request.getReplyToEmail();
            if ((replyTo == null || replyTo.isBlank()) && member != null) {
                replyTo = member.getEmail();
            }
            if (replyTo != null && !replyTo.isBlank()) {
                helper.setReplyTo(replyTo);
            }

            String subject = "[Support] " + request.getSubject();
            helper.setSubject(subject);

            StringBuilder body = new StringBuilder();
            body.append("Organization: ")
                .append(organization != null ? organization.getOrgName() : "N/A")
                .append("\n");
            body.append("Org ID: ")
                .append(organization != null ? organization.getOrgId() : "N/A")
                .append("\n");
            body.append("User: ")
                .append(member != null ? member.getName() : "N/A")
                .append(" (ID: ")
                .append(member != null ? member.getMemberId() : "N/A")
                .append(")\n");
            body.append("User Email: ")
                .append(member != null ? member.getEmail() : "N/A")
                .append("\n\n");

            body.append("Message:\n");
            body.append(request.getMessage() != null ? request.getMessage() : "");

            helper.setText(body.toString(), false);

            mailSender.send(message);
            log.info("Support email sent to {} for member {}", toAddress,
                    member != null ? member.getMemberId() : "N/A");
        } catch (MessagingException e) {
            log.error("Failed to send support email: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send support email: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error sending support email: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error sending support email: " + e.getMessage(), e);
        }
    }
}
