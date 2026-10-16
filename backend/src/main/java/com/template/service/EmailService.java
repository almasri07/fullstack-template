package com.template.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    // Currently a log-only stub. To send real emails via SMTP:
    //   1. Add the `spring-boot-starter-mail` dependency to pom.xml.
    //   2. Configure spring.mail.* in application.yml (host, port, username, password,
    //      properties.mail.smtp.auth=true, properties.mail.smtp.starttls.enable=true) —
    //      pull credentials from env vars, never hardcode them.
    //   3. Inject `JavaMailSender` here via constructor (it's auto-configured once step 1/2
    //      are in place) instead of/alongside the logger.
    //   4. Build the message (SimpleMailMessage, or MimeMessageHelper for HTML) with the
    //      reset link embedding the token, e.g. https://<frontend>/reset-password?token=...
    //   5. Call mailSender.send(message).
    //   6. Wrap the send in try/catch for MailException — log the failure but don't leak
    //      SMTP errors back to the caller (requestPasswordReset's response stays the same
    //      either way, so you don't reveal whether an email account exists/send failed).
    public void sendPasswordResetEmail(String to, String token) {
        log.info("Password reset requested for {} — token: {}", to, token);
    }
}
