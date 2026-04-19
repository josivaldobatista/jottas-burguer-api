package com.jfb.jottasburger.order.model;

import com.jfb.jottasburger.exception.InvalidOrderStatusTransitionException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderItem> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Order(String orderNumber) {
        this.orderNumber = orderNumber;
        this.status = OrderStatus.RECEIVED;
        this.totalAmount = BigDecimal.ZERO;
    }

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
        item.setOrder(this);
        recalculateTotalAmount();
    }

    public void updateStatus(OrderStatus newStatus) {
        if (this.status == newStatus) {
            throw new InvalidOrderStatusTransitionException(
                    "Order is already in status " + newStatus
            );
        }

        if (!this.status.canTransitionTo(newStatus)) {
            throw new InvalidOrderStatusTransitionException(
                    "Cannot change order status from " + this.status + " to " + newStatus
            );
        }

        this.status = newStatus;
    }

    private void recalculateTotalAmount() {
        this.totalAmount = this.items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}