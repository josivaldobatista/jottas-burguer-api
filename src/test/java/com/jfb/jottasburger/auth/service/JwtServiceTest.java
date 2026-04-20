package com.jfb.jottasburger.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        String secret = "change-this-secret-key-change-this-secret-key-change-this-secret-key";
        long expiration = 3600000L;

        jwtService = new JwtService(secret, expiration);

        userDetails = User.withUsername("admin@jottasburger.com")
                .password("encoded-password")
                .authorities("ROLE_ADMIN")
                .build();
    }

    @Test
    void shouldGenerateTokenSuccessfully() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldExtractUsernameSuccessfully() {
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertEquals("admin@jottasburger.com", username);
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        String token = jwtService.generateToken(userDetails);

        boolean valid = jwtService.isTokenValid(token, userDetails);

        assertTrue(valid);
    }

    @Test
    void shouldReturnFalseWhenTokenBelongsToAnotherUser() {
        String token = jwtService.generateToken(userDetails);

        UserDetails anotherUser = User.withUsername("other@jottasburger.com")
                .password("encoded-password")
                .authorities("ROLE_ADMIN")
                .build();

        boolean valid = jwtService.isTokenValid(token, anotherUser);

        assertFalse(valid);
    }
}