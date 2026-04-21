package com.jfb.jottasburger.order.service;

import com.jfb.jottasburger.auth.service.AuthenticatedUserService;
import com.jfb.jottasburger.exception.BusinessException;
import com.jfb.jottasburger.exception.ResourceNotFoundException;
import com.jfb.jottasburger.order.dto.*;
import com.jfb.jottasburger.order.model.Order;
import com.jfb.jottasburger.order.model.OrderItem;
import com.jfb.jottasburger.order.repository.OrderItemRepository;
import com.jfb.jottasburger.order.repository.OrderRepository;
import com.jfb.jottasburger.order.repository.OrderSpecifications;
import com.jfb.jottasburger.product.model.Product;
import com.jfb.jottasburger.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderNumberGenerator orderNumberGenerator;
    private final AuthenticatedUserService authenticatedUserService;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        var authenticatedUser = authenticatedUserService.getAuthenticatedUser();

        Order order = new Order(orderNumberGenerator.generate(), authenticatedUser);

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

    @Transactional(readOnly = true)
    public Page<OrderResponse> findAll(OrderFilterRequest filter, Pageable pageable) {
        var specification = Specification.allOf(
                OrderSpecifications.hasStatus(filter.status())
        );

        var page = orderRepository.findAll(specification, pageable);
        var responses = toResponseList(page.getContent());

        return new PageImpl<>(
                responses,
                pageable,
                page.getTotalElements()
        );
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findMyOrders() {
        var authenticatedUser = authenticatedUserService.getAuthenticatedUser();

        return orderRepository.findByUserIdOrderByCreatedAtDesc(authenticatedUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> findMyOrders(OrderFilterRequest filter, Pageable pageable) {
        var authenticatedUser = authenticatedUserService.getAuthenticatedUser();

        var specification = Specification.allOf(
                OrderSpecifications.hasUserId(authenticatedUser.getId()),
                OrderSpecifications.hasStatus(filter.status())
        );

        var page = orderRepository.findAll(specification, pageable);
        var responses = toResponseList(page.getContent());

        return new PageImpl<>(
                responses,
                pageable,
                page.getTotalElements()
        );
    }

    @Transactional
    public OrderResponse updateStatus(Long id, UpdateOrderStatusRequest request) {
        Order order = findOrderById(id);
        order.updateStatus(request.status());
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

    private List<OrderResponse> toResponseList(List<Order> orders) {
        List<Long> orderIds = orders.stream()
                .map(Order::getId)
                .toList();

        var itemsByOrderId = orderItemRepository.findByOrderIdIn(orderIds)
                .stream()
                .collect(Collectors.groupingBy(item -> item.getOrder().getId()));

        return orders.stream()
                .map(order -> toResponse(order, itemsByOrderId.getOrDefault(order.getId(), List.of())))
                .toList();
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

    private OrderResponse toResponse(Order order, List<OrderItem> items) {
        List<OrderItemResponse> itemResponses = items.stream()
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