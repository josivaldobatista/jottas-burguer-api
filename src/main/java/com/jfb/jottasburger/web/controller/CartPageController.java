package com.jfb.jottasburger.web.controller;

import com.jfb.jottasburger.auth.service.AuthenticatedUserService;
import com.jfb.jottasburger.order.dto.CreateOrderItemRequest;
import com.jfb.jottasburger.order.dto.CreateOrderRequest;
import com.jfb.jottasburger.order.dto.OrderResponse;
import com.jfb.jottasburger.order.service.OrderService;
import com.jfb.jottasburger.product.dto.ProductResponse;
import com.jfb.jottasburger.product.service.ProductService;
import com.jfb.jottasburger.user.model.User;
import com.jfb.jottasburger.web.dto.OrderSuccessView;
import com.jfb.jottasburger.web.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartPageController {

    private final CartService cartService;
    private final ProductService productService;
    private final AuthenticatedUserService authenticatedUserService;
    private final OrderService orderService;

    @GetMapping("/carrinho")
    public String cartPage(Model model, HttpSession session) {
        User user = authenticatedUserService.getAuthenticatedUser();

        model.addAttribute("pageTitle", "Carrinho - Jotta's Burger");
        model.addAttribute("userName", user.getName());
        model.addAttribute("cartItems", cartService.getCartItems(session));
        model.addAttribute("totalAmount", cartService.getTotalAmount(session));

        return "pages/cart";
    }

    @PostMapping("/carrinho/adicionar")
    public String addToCart(@RequestParam Long productId, HttpSession session) {
        ProductResponse product = productService.findById(productId);
        cartService.addItem(session, product);

        return "redirect:/carrinho";
    }

    @PostMapping("/carrinho/finalizar")
    public String finishOrder(HttpSession session, Model model) {
        if (cartService.isEmpty(session)) {
            return "redirect:/carrinho";
        }

        List<CreateOrderItemRequest> items = cartService.getCartItems(session).stream()
                .map(item -> new CreateOrderItemRequest(
                        item.getProductId(),
                        item.getQuantity()
                ))
                .toList();

        CreateOrderRequest request = new CreateOrderRequest(items);

        OrderResponse order = orderService.create(request);

        cartService.clear(session);

        model.addAttribute("order", new OrderSuccessView(
                order.orderNumber(),
                order.status().name(),
                order.totalAmount()
        ));

        return "pages/order-success";
    }

    @PostMapping("/carrinho/aumentar")
    public String increase(@RequestParam Long productId, HttpSession session) {
        cartService.increase(productId, session);
        return "redirect:/carrinho";
    }

    @PostMapping("/carrinho/diminuir")
    public String decrease(@RequestParam Long productId, HttpSession session) {
        cartService.decrease(productId, session);
        return "redirect:/carrinho";
    }

    @PostMapping("/carrinho/remover")
    public String remove(@RequestParam Long productId, HttpSession session) {
        cartService.remove(productId, session);
        return "redirect:/carrinho";
    }
}