package com.jfb.jottasburger.user.service;

import com.jfb.jottasburger.auth.service.AuthenticatedUserService;
import com.jfb.jottasburger.exception.BusinessException;
import com.jfb.jottasburger.user.dto.ChangePasswordRequest;
import com.jfb.jottasburger.user.dto.UpdateUserMeRequest;
import com.jfb.jottasburger.user.dto.UserMeResponse;
import com.jfb.jottasburger.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticatedUserService authenticatedUserService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserMeResponse getMe() {
        User user = authenticatedUserService.getAuthenticatedUser();
        return toMeResponse(user);
    }

    @Transactional
    public UserMeResponse updateMe(UpdateUserMeRequest request) {
        User user = authenticatedUserService.getAuthenticatedUser();
        user.updateName(request.name().trim());
        return toMeResponse(user);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = authenticatedUserService.getAuthenticatedUser();

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BusinessException("Current password is incorrect");
        }

        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new BusinessException("New password and confirmation do not match");
        }

        if (request.currentPassword().equals(request.newPassword())) {
            throw new BusinessException("New password must be different from current password");
        }

        user.updatePassword(passwordEncoder.encode(request.newPassword()));
    }

    private UserMeResponse toMeResponse(User user) {
        return new UserMeResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}