package com.jfb.jottasburger.auth.service;

import com.jfb.jottasburger.auth.dto.LoginRequest;
import com.jfb.jottasburger.auth.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JpaUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token, "Bearer", 3600L);
    }
}