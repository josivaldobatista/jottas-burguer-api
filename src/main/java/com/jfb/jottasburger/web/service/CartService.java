package com.jfb.jottasburger.web.service;

import com.jfb.jottasburger.product.dto.ProductResponse;
import com.jfb.jottasburger.web.dto.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private static final String CART_SESSION_KEY = "cartItems";

    @SuppressWarnings("unchecked")
    public List<CartItem> getCartItems(HttpSession session) {
        Object attribute = session.getAttribute(CART_SESSION_KEY);

        if (attribute == null) {
            List<CartItem> emptyCart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, emptyCart);
            return emptyCart;
        }

        return (List<CartItem>) attribute;
    }

    public void addItem(HttpSession session, ProductResponse product) {
        List<CartItem> cartItems = getCartItems(session);

        Optional<CartItem> existingItem = cartItems.stream()
                .filter(item -> item.getProductId().equals(product.id()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().increaseQuantity();
        } else {
            cartItems.add(new CartItem(
                    product.id(),
                    product.name(),
                    product.price(),
                    1
            ));
        }

        session.setAttribute(CART_SESSION_KEY, cartItems);
    }

    public BigDecimal getTotalAmount(HttpSession session) {
        return getCartItems(session).stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEmpty(HttpSession session) {
        return getCartItems(session).isEmpty();
    }

    public void clear(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public void increase(Long productId, HttpSession session) {
        getCartItems(session).stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresent(CartItem::increaseQuantity);
    }

    public void decrease(Long productId, HttpSession session) {
        List<CartItem> items = getCartItems(session);

        items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    if (item.getQuantity() > 1) {
                        item.decreaseQuantity();
                    } else {
                        items.remove(item);
                    }
                });
    }

    public void remove(Long productId, HttpSession session) {
        getCartItems(session).removeIf(item -> item.getProductId().equals(productId));
    }
}