package magelan.orders.order.dto;

import magelan.orders.order.model.OrderItem;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID itemId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal
) {

    public static OrderItemResponse from(OrderItem item) {
        BigDecimal lineTotal = item.getUnitPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));

        return new OrderItemResponse(
                item.getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                lineTotal
        );
    }
}