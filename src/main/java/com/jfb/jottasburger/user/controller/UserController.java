package com.jfb.jottasburger.user.controller;

import com.jfb.jottasburger.user.dto.ChangePasswordRequest;
import com.jfb.jottasburger.user.dto.UpdateUserMeRequest;
import com.jfb.jottasburger.user.dto.UserMeResponse;
import com.jfb.jottasburger.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> getMe() {
        return ResponseEntity.ok(userService.getMe());
    }

    @PutMapping("/me")
    public ResponseEntity<UserMeResponse> updateMe(@Valid @RequestBody UpdateUserMeRequest request) {
        return ResponseEntity.ok(userService.updateMe(request));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.noContent().build();
    }
}