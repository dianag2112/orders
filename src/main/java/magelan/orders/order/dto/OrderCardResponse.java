package magelan.orders.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import magelan.orders.order.model.Order;
import magelan.orders.order.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderCardResponse {

    private UUID orderId;
    private String orderName;
    private BigDecimal amount;
    private int totalItems;
    private List<OrderItemResponse> items;

    public static OrderCardResponse from(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(OrderItemResponse::from)
                .toList();

        int totalItems = order.getItems().stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();

        return new OrderCardResponse(
                order.getId(),
                order.getOrderName(),
                order.getAmount(),
                totalItems,
                itemResponses
        );
    }
}