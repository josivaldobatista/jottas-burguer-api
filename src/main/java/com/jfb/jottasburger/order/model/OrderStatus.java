package com.jfb.jottasburger.order.model;

import java.util.Set;

public enum OrderStatus {

    RECEIVED,
    IN_PREPARATION,
    READY,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELED;

    public boolean canTransitionTo(OrderStatus newStatus) {
        return switch (this) {
            case RECEIVED -> Set.of(IN_PREPARATION, CANCELED).contains(newStatus);
            case IN_PREPARATION -> Set.of(READY, CANCELED).contains(newStatus);
            case READY -> Set.of(OUT_FOR_DELIVERY, CANCELED).contains(newStatus);
            case OUT_FOR_DELIVERY -> Set.of(DELIVERED).contains(newStatus);
            case DELIVERED, CANCELED -> false;
        };
    }
}