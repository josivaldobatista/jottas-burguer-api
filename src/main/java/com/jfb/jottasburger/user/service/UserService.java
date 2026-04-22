package com.jfb.jottasburger.user.service;

import com.jfb.jottasburger.auth.service.AuthenticatedUserService;
import com.jfb.jottasburger.user.dto.UserMeResponse;
import com.jfb.jottasburger.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticatedUserService authenticatedUserService;

    @Transactional(readOnly = true)
    public UserMeResponse getMe() {
        var user = authenticatedUserService.getAuthenticatedUser();
        return toMeResponse(user);
    }

    private UserMeResponse toMeResponse(User entity) {
        return new UserMeResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getRole(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
