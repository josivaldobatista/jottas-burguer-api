package com.jfb.jottasburger.order.repository;

import com.jfb.jottasburger.order.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderIdIn(List<Long> orderIds);
}