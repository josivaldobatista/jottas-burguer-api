package com.jfb.jottasburger.order.repository;

import com.jfb.jottasburger.order.model.Order;
import com.jfb.jottasburger.order.model.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

public final class OrderSpecifications {

    private OrderSpecifications() {
    }

    public static Specification<Order> hasStatus(OrderStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }
}