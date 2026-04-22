package com.jfb.jottasburger.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmtpEmailService implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendWelcomeEmail(String name, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Welcome to JottasBurger");
        message.setText("""
                Hello, %s!

                Welcome to JottasBurger 🍔

                Your account has been created successfully.
                You can now log in and place your orders.

                Best regards,
                JottasBurger Team
                """.formatted(name));

        mailSender.send(message);

        log.info("Welcome email sent to {}", email);
    }
}