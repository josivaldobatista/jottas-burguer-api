package com.jfb.jottasburger.order.service;

import com.jfb.jottasburger.exception.BusinessException;
import com.jfb.jottasburger.exception.ResourceNotFoundException;
import com.jfb.jottasburger.order.dto.CreateOrderItemRequest;
import com.jfb.jottasburger.order.dto.CreateOrderRequest;
import com.jfb.jottasburger.order.dto.OrderResponse;
import com.jfb.jottasburger.order.dto.UpdateOrderStatusRequest;
import com.jfb.jottasburger.order.model.Order;
import com.jfb.jottasburger.order.model.OrderStatus;
import com.jfb.jottasburger.order.repository.OrderRepository;
import com.jfb.jottasburger.product.model.Product;
import com.jfb.jottasburger.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderNumberGenerator orderNumberGenerator;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateOrderSuccessfully() {
        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new CreateOrderItemRequest(1L, 2))
        );

        Product product = mock(Product.class);
        when(product.getId()).thenReturn(1L);
        when(product.getName()).thenReturn("X-Burger");
        when(product.getPrice()).thenReturn(new BigDecimal("25.00"));
        when(product.isActive()).thenReturn(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderNumberGenerator.generate()).thenReturn("ORD-123");
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.create(request);

        assertEquals("ORD-123", response.orderNumber());
        assertEquals(OrderStatus.RECEIVED, response.status());
        assertEquals(new BigDecimal("50.00"), response.totalAmount());
        assertEquals(1, response.items().size());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {
        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new CreateOrderItemRequest(99L, 1))
        );

        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        when(orderNumberGenerator.generate()).thenReturn("ORD-123");

        assertThrows(ResourceNotFoundException.class, () -> orderService.create(request));
    }

    @Test
    void shouldThrowWhenProductIsInactive() {
        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new CreateOrderItemRequest(1L, 1))
        );

        Product product = mock(Product.class);
        when(product.isActive()).thenReturn(false);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderNumberGenerator.generate()).thenReturn("ORD-123");

        assertThrows(BusinessException.class, () -> orderService.create(request));
    }

    @Test
    void shouldUpdateOrderStatusSuccessfully() {
        Order order = new Order("ORD-123");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.updateStatus(
                1L,
                new UpdateOrderStatusRequest(OrderStatus.IN_PREPARATION)
        );

        assertEquals(OrderStatus.IN_PREPARATION, response.status());
    }
}