package com.jfb.jottasburger.web.controller;

import com.jfb.jottasburger.auth.service.AuthenticatedUserService;
import com.jfb.jottasburger.category.dto.CategoryResponse;
import com.jfb.jottasburger.category.service.CategoryService;
import com.jfb.jottasburger.product.dto.ProductResponse;
import com.jfb.jottasburger.product.service.ProductService;
import com.jfb.jottasburger.user.model.User;
import com.jfb.jottasburger.web.dto.HomeCategorySection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomePageController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final AuthenticatedUserService authenticatedUserService;

    @GetMapping("/home")
    public String home(Model model) {
        User user = authenticatedUserService.getAuthenticatedUser();

        List<CategoryResponse> categories = categoryService.findAllActive();
        List<ProductResponse> products = productService.findAllActive();

        List<HomeCategorySection> sections = categories.stream()
                .map(category -> new HomeCategorySection(
                        category.id(),
                        category.name(),
                        products.stream()
                                .filter(product -> product.categoryId().equals(category.id()))
                                .toList()
                ))
                .toList();

        model.addAttribute("pageTitle", "Home - Jotta's Burger");
        model.addAttribute("userName", user.getName());
        model.addAttribute("sections", sections);

        return "pages/home";
    }
}