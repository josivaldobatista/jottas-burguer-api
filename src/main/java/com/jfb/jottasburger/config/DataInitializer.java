package com.jfb.jottasburger.config;

import com.jfb.jottasburger.user.model.Role;
import com.jfb.jottasburger.user.model.User;
import com.jfb.jottasburger.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev", "local"})
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmailIgnoreCase(adminEmail).isEmpty()) {

            User admin = new User(
                    "Admin",
                    adminEmail,
                    passwordEncoder.encode(adminPassword),
                    Role.ADMIN
            );

            userRepository.save(admin);

            System.out.println("Admin for DEV profile user created");
        }
    }
}