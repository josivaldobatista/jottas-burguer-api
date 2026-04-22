package com.jfb.jottasburger.user.service;

import com.jfb.jottasburger.auth.service.AuthenticatedUserService;
import com.jfb.jottasburger.user.dto.UpdateUserMeRequest;
import com.jfb.jottasburger.user.dto.UserMeResponse;
import com.jfb.jottasburger.user.model.User;
import com.jfb.jottasburger.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticatedUserService authenticatedUserService;
    private final UserRepository repository;

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