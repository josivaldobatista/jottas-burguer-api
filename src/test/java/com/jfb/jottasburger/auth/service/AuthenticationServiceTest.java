package com.jfb.jottasburger.auth.service;

import com.jfb.jottasburger.auth.dto.LoginRequest;
import com.jfb.jottasburger.auth.dto.LoginResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JpaUserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest(
                "admin@jottasburger.com",
                "Admin@123"
        );

        UserDetails userDetails = User.withUsername("admin@jottasburger.com")
                .password("encoded-password")
                .authorities("ROLE_ADMIN")
                .build();

        when(userDetailsService.loadUserByUsername("admin@jottasburger.com"))
                .thenReturn(userDetails);

        when(jwtService.generateToken(userDetails))
                .thenReturn("mocked-jwt-token");

        LoginResponse response = authenticationService.login(request);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.accessToken());
        assertEquals("Bearer", response.tokenType());
        assertEquals(3600L, response.expiresIn());

        verify(authenticationManager).authenticate(any());
        verify(userDetailsService).loadUserByUsername("admin@jottasburger.com");
        verify(jwtService).generateToken(userDetails);
    }
}