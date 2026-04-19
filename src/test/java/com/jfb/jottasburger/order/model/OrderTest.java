package com.jfb.jottasburger.order.model;

import com.jfb.jottasburger.exception.InvalidOrderStatusTransitionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    @Test
    void shouldStartWithReceivedStatus() {
        Order order = new Order("ORD-123");

        assertEquals(OrderStatus.RECEIVED, order.getStatus());
    }

    @Test
    void shouldAllowTransitionFromReceivedToInPreparation() {
        Order order = new Order("ORD-123");

        order.updateStatus(OrderStatus.IN_PREPARATION);

        assertEquals(OrderStatus.IN_PREPARATION, order.getStatus());
    }

    @Test
    void shouldAllowTransitionFromReceivedToCanceled() {
        Order order = new Order("ORD-123");

        order.updateStatus(OrderStatus.CANCELED);

        assertEquals(OrderStatus.CANCELED, order.getStatus());
    }

    @Test
    void shouldNotAllowInvalidTransitionFromReceivedToDelivered() {
        Order order = new Order("ORD-123");

        assertThrows(
                InvalidOrderStatusTransitionException.class,
                () -> order.updateStatus(OrderStatus.DELIVERED)
        );
    }

    @Test
    void shouldNotAllowSameStatusTransition() {
        Order order = new Order("ORD-123");

        assertThrows(
                InvalidOrderStatusTransitionException.class,
                () -> order.updateStatus(OrderStatus.RECEIVED)
        );
    }

    @Test
    void shouldNotAllowTransitionAfterDelivered() {
        Order order = new Order("ORD-123");

        order.updateStatus(OrderStatus.IN_PREPARATION);
        order.updateStatus(OrderStatus.READY);
        order.updateStatus(OrderStatus.OUT_FOR_DELIVERY);
        order.updateStatus(OrderStatus.DELIVERED);

        assertThrows(
                InvalidOrderStatusTransitionException.class,
                () -> order.updateStatus(OrderStatus.CANCELED)
        );
    }
}