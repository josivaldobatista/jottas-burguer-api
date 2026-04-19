package com.jfb.jottasburger.order.service;

import com.jfb.jottasburger.exception.BusinessException;
import com.jfb.jottasburger.exception.ResourceNotFoundException;
import com.jfb.jottasburger.order.dto.CreateOrderItemRequest;
import com.jfb.jottasburger.order.dto.CreateOrderRequest;
import com.jfb.jottasburger.order.dto.OrderItemResponse;
import com.jfb.jottasburger.order.dto.OrderResponse;
import com.jfb.jottasburger.order.dto.UpdateOrderStatusRequest;
import com.jfb.jottasburger.order.model.Order;
import com.jfb.jottasburger.order.model.OrderItem;
import com.jfb.jottasburger.product.model.Product;
import com.jfb.jottasburger.product.repository.ProductRepository;
import com.jfb.jottasburger.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderNumberGenerator orderNumberGenerator;

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        Order order = new Order(orderNumberGenerator.generate());

        for (CreateOrderItemRequest itemRequest : request.items()) {
            Product product = findActiveProductById(itemRequest.productId());

            OrderItem orderItem = new OrderItem(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    itemRequest.quantity()
            );

            order.addItem(orderItem);
        }

        Order savedOrder = orderRepository.save(order);
        return toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        Order order = findOrderById(id);
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public OrderResponse updateStatus(Long id, UpdateOrderStatusRequest request) {
        Order order = findOrderById(id);

        try {
            order.updateStatus(request.status());
        } catch (IllegalStateException ex) {
            throw new BusinessException(ex.getMessage());
        }

        return toResponse(order);
    }

    private Product findActiveProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (!product.isActive()) {
            throw new BusinessException("Inactive product cannot be added to an order");
        }

        return product;
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems()
                .stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getProductId(),
                        item.getProductName(),
                        item.getUnitPrice(),
                        item.getQuantity(),
                        item.getTotalPrice()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getTotalAmount(),
                itemResponses,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}