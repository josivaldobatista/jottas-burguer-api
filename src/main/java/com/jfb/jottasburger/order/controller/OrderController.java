package com.jfb.jottasburger.order.controller;

import com.jfb.jottasburger.common.dto.PageResponse;
import com.jfb.jottasburger.order.dto.CreateOrderRequest;
import com.jfb.jottasburger.order.dto.OrderFilterRequest;
import com.jfb.jottasburger.order.dto.OrderResponse;
import com.jfb.jottasburger.order.dto.UpdateOrderStatusRequest;
import com.jfb.jottasburger.order.model.OrderStatus;
import com.jfb.jottasburger.order.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Operations for managing orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrderResponse>> findAll(
            @RequestParam(required = false) OrderStatus status,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        OrderFilterRequest filter = new OrderFilterRequest(status);
        return ResponseEntity.ok(PageResponse.from(orderService.findAll(filter, pageable)));
    }

    @GetMapping("/me")
    public ResponseEntity<PageResponse<OrderResponse>> findMyOrders(
            @RequestParam(required = false) OrderStatus status,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        OrderFilterRequest filter = new OrderFilterRequest(status);
        return ResponseEntity.ok(PageResponse.from(orderService.findMyOrders(filter, pageable)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        return ResponseEntity.ok(orderService.updateStatus(id, request));
    }
}