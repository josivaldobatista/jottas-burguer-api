package com.jfb.jottasburger.web.controller;

import com.jfb.jottasburger.auth.service.AuthenticatedUserService;
import com.jfb.jottasburger.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProfilePageController {

    private final AuthenticatedUserService authenticatedUserService;

    @GetMapping("/perfil")
    public String profile(Model model) {
        User user = authenticatedUserService.getAuthenticatedUser();

        model.addAttribute("pageTitle", "Perfil - Jotta's Burger");
        model.addAttribute("user", user);

        return "pages/profile";
    }
}