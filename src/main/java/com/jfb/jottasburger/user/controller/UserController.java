package com.jfb.jottasburger.user.controller;

import com.jfb.jottasburger.user.dto.ChangePasswordRequest;
import com.jfb.jottasburger.user.dto.UpdateUserMeRequest;
import com.jfb.jottasburger.user.dto.UserMeResponse;
import com.jfb.jottasburger.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Authenticated user operations")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get authenticated user profile")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserMeResponse> getMe() {
        return ResponseEntity.ok(userService.getMe());
    }

    @PutMapping("/me")
    @Operation(summary = "Update authenticated user profile")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserMeResponse> updateMe(@Valid @RequestBody UpdateUserMeRequest request) {
        return ResponseEntity.ok(userService.updateMe(request));
    }

    @PatchMapping("/me/password")
    @Operation(summary = "Change authenticated user password")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.noContent().build();
    }
}