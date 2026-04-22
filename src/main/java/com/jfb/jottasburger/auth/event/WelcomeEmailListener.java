package com.jfb.jottasburger.auth.event;

import com.jfb.jottasburger.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WelcomeEmailListener {

    private final EmailService emailService;

    @EventListener
    public void handle(UserRegisteredEvent event) {
        try {
            log.info("Handling UserRegisteredEvent for userId={} email={}", event.userId(), event.email());
            emailService.sendWelcomeEmail(event.name(), event.email());
        } catch (Exception ex) {
            log.error("Failed to send welcome email to {}", event.email(), ex);
        }
    }
}