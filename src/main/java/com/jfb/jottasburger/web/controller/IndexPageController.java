package com.jfb.jottasburger.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexPageController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Jotta's Burger");
        return "pages/index";
    }
}