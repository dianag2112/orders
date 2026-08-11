package magelan.orders.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import magelan.orders.order.dto.OrderCardResponse;
import magelan.orders.order.dto.OrderItemResponse;
import magelan.orders.order.model.Order;
import magelan.orders.order.model.OrderItem;
import magelan.orders.order.model.OrderStatus;
import magelan.orders.order.repository.OrderItemRepository;
import magelan.orders.order.repository.OrderRepository;
import magelan.orders.product.model.Product;
import magelan.orders.product.repository.ProductRepository;
import magelan.orders.revenue.service.DailyRevenueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private static final BigDecimal ONE =
            BigDecimal.ONE;

    private static final BigDecimal HALF =
            new BigDecimal("0.5");

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    /*
     * Handles the restaurant's revenue history and
     * Europe/Sofia business-day calculations.
     */
    private final DailyRevenueService dailyRevenueService;


    @Transactional
    public Order createPendingOrder(String orderName) {

        Order order = Order.builder()
                .orderName(orderName)
                .orderStatus(OrderStatus.PENDING)
                .amount(BigDecimal.ZERO)
                .createdOn(
                        dailyRevenueService
                                .getCurrentSofiaDateTime()
                )
                .build();

        return orderRepository.save(order);
    }


    @Transactional(readOnly = true)
    public List<Order> getPendingOrders() {

        return orderRepository
                .findAllByOrderStatusOrderByCreatedOnDesc(
                        OrderStatus.PENDING
                );
    }


    @Transactional(readOnly = true)
    public List<Order> getCompletedOrders() {

        return orderRepository
                .findAllByOrderStatusOrderByCreatedOnAsc(
                        OrderStatus.COMPLETED
                )
                .stream()
                .sorted(
                        Comparator.comparing(
                                (Order order) ->
                                        order.getCompletedOn() != null
                                                ? order.getCompletedOn()
                                                : order.getCreatedOn()
                        ).reversed()
                )
                .toList();
    }


    @Transactional(readOnly = true)
    public Order getById(UUID orderId) {

        return orderRepository
                .findById(orderId)
                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "Order not found: "
                                                + orderId
                                )
                );
    }


    @Transactional
    public void addProductToOrder(
            UUID orderId,
            UUID productId
    ) {

        Order order =
                getById(orderId);

        if (
                order.getOrderStatus()
                        != OrderStatus.PENDING
        ) {

            throw new IllegalStateException(
                    "Items can only be added to pending orders."
            );
        }


        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Product not found: "
                                                        + productId
                                        )
                        );


        OrderItem item =
                orderItemRepository
                        .findByOrderAndProduct(
                                order,
                                product
                        )
                        .orElseGet(
                                () -> {

                                    OrderItem newItem =
                                            OrderItem.builder()
                                                    .order(order)
                                                    .product(product)
                                                    .quantity(BigDecimal.ZERO)
                                                    .unitPrice(
                                                            product.getPrice()
                                                    )
                                                    .createdOn(
                                                            dailyRevenueService
                                                                    .getCurrentSofiaDateTime()
                                                    )
                                                    .build();

                                    order.getItems()
                                            .add(newItem);

                                    return newItem;
                                }
                        );


        item.setQuantity(
                item.getQuantity()
                        .add(ONE)
        );

        item.setUnitPrice(
                product.getPrice()
        );


        recalculateAmount(order);

        orderRepository.save(order);
    }


    @Transactional
    public void removeItem(UUID itemId) {

        OrderItem item =
                orderItemRepository
                        .findById(itemId)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Order item not found: "
                                                        + itemId
                                        )
                        );

        Order order =
                item.getOrder();

        order.getItems()
                .remove(item);

        orderItemRepository.delete(item);

        recalculateAmount(order);

        orderRepository.save(order);
    }


    /*
     * Completes an order and records its revenue.
     *
     * IMPORTANT:
     *
     * The order and revenue update happen inside the
     * same transaction.
     *
     * The completion time is Europe/Sofia time.
     *
     * An order completed between 00:00 and 05:59
     * belongs to the previous business day.
     */
    @Transactional
    public void completeOrder(UUID orderId) {

        Order order =
                getById(orderId);


        /*
         * Prevent accidentally recording the same
         * completed order twice.
         */
        if (
                order.getOrderStatus()
                        == OrderStatus.COMPLETED
        ) {

            log.warn(
                    "Order {} is already completed. "
                            + "Revenue will not be recorded again.",
                    orderId
            );

            return;
        }


        if (
                order.getOrderStatus()
                        != OrderStatus.PENDING
        ) {

            throw new IllegalStateException(
                    "Only pending orders can be completed."
            );
        }


        /*
         * Make absolutely sure the final amount is
         * correct before recording the sale.
         */
        recalculateAmount(order);


        LocalDateTime completedAt =
                dailyRevenueService
                        .getCurrentSofiaDateTime();


        order.setOrderStatus(
                OrderStatus.COMPLETED
        );

        order.setCompletedOn(
                completedAt
        );


        orderRepository.save(order);


        /*
         * Permanently add this sale to the appropriate
         * business day's revenue.
         */
        dailyRevenueService
                .recordCompletedOrder(
                        order.getAmount(),
                        completedAt
                );


        log.info(
                "Completed order {} with amount {} at {}",
                orderId,
                order.getAmount(),
                completedAt
        );
    }


    @Transactional
    public void cancelOrder(UUID orderId) {

        Order order =
                getById(orderId);

        order.setOrderStatus(
                OrderStatus.CANCELLED
        );

        orderRepository.save(order);
    }


    private void recalculateAmount(
            Order order
    ) {

        BigDecimal total =
                order.getItems()
                        .stream()
                        .map(
                                OrderItem::getTotalPrice
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        order.setAmount(total);
    }


    @Transactional
    public Order increaseItemQuantity(
            UUID itemId
    ) {

        OrderItem item =
                orderItemRepository
                        .findById(itemId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Order item not found"
                                        )
                        );


        item.setQuantity(
                item.getQuantity()
                        .add(HALF)
        );

        orderItemRepository.save(item);


        Order order =
                item.getOrder();

        recalculateAmount(order);

        orderRepository.save(order);


        return orderRepository
                .findById(
                        order.getId()
                )
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "Order not found"
                                )
                );
    }


    @Transactional
    public Order decreaseItemQuantity(
            UUID itemId
    ) {

        OrderItem item =
                orderItemRepository
                        .findById(itemId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Order item not found"
                                        )
                        );


        Order order =
                item.getOrder();


        if (
                item.getQuantity()
                        .compareTo(HALF)
                        <= 0
        ) {

            order.getItems()
                    .remove(item);

            orderItemRepository
                    .delete(item);


            recalculateAmount(order);

            orderRepository.save(order);


            return orderRepository
                    .findById(
                            order.getId()
                    )
                    .orElseThrow(
                            () ->
                                    new IllegalArgumentException(
                                            "Order not found"
                                    )
                    );
        }


        item.setQuantity(
                item.getQuantity()
                        .subtract(HALF)
        );

        orderItemRepository.save(item);


        recalculateAmount(order);

        orderRepository.save(order);


        return orderRepository
                .findById(
                        order.getId()
                )
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "Order not found"
                                )
                );
    }


    private OrderCardResponse mapToOrderCardResponse(
            Order order
    ) {

        List<OrderItemResponse> items =
                order.getItems()
                        .stream()
                        .map(
                                item ->
                                        new OrderItemResponse(
                                                item.getId(),
                                                item.getProduct()
                                                        .getName(),
                                                item.getQuantity(),
                                                item.getUnitPrice(),
                                                item.getTotalPrice()
                                        )
                        )
                        .toList();


        BigDecimal totalItems =
                order.getItems()
                        .stream()
                        .map(
                                OrderItem::getQuantity
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );


        return new OrderCardResponse(
                order.getId(),
                order.getOrderName(),
                order.getAmount(),
                totalItems,
                items
        );
    }


    @Transactional
    public OrderCardResponse removeItemAndReturnOrderCard(
            UUID itemId
    ) {

        OrderItem item =
                orderItemRepository
                        .findById(itemId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Item not found"
                                        )
                        );


        Order order =
                item.getOrder();


        order.getItems()
                .remove(item);

        orderItemRepository
                .delete(item);


        recalculateAmount(order);

        orderRepository.save(order);


        return mapToOrderCardResponse(
                order
        );
    }


    @Transactional
    public OrderCardResponse increaseItemQuantityAndReturnOrderCard(
            UUID itemId
    ) {

        OrderItem item =
                orderItemRepository
                        .findById(itemId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Item not found"
                                        )
                        );


        item.setQuantity(
                item.getQuantity()
                        .add(HALF)
        );

        orderItemRepository.save(item);


        Order order =
                item.getOrder();


        recalculateAmount(order);

        orderRepository.save(order);


        return mapToOrderCardResponse(
                order
        );
    }


    @Transactional
    public OrderCardResponse decreaseItemQuantityAndReturnOrderCard(
            UUID itemId
    ) {

        OrderItem item =
                orderItemRepository
                        .findById(itemId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Item not found"
                                        )
                        );


        Order order =
                item.getOrder();


        if (
                item.getQuantity()
                        .compareTo(HALF)
                        <= 0
        ) {

            order.getItems()
                    .remove(item);

            orderItemRepository
                    .delete(item);

        } else {

            item.setQuantity(
                    item.getQuantity()
                            .subtract(HALF)
            );

            orderItemRepository
                    .save(item);
        }


        recalculateAmount(order);

        orderRepository.save(order);


        return mapToOrderCardResponse(
                order
        );
    }


    @Transactional(readOnly = true)
    public OrderCardResponse getPendingOrderCard(
            UUID orderId
    ) {

        Order order =
                getById(orderId);


        if (
                order.getOrderStatus()
                        != OrderStatus.PENDING
        ) {

            throw new IllegalStateException(
                    "Only pending orders can be opened from the menu."
            );
        }


        return mapToOrderCardResponse(
                order
        );
    }


    @Transactional(readOnly = true)
    public Order getCompletedOrderById(
            UUID orderId
    ) {

        Order order =
                getById(orderId);


        if (
                order.getOrderStatus()
                        != OrderStatus.COMPLETED
        ) {

            throw new IllegalStateException(
                    "A receipt can only be generated for a completed order."
            );
        }


        return order;
    }


    /*
     * Permanently deletes the completed order itself.
     *
     * Revenue is deliberately NOT changed here.
     *
     * Once money has been recorded in daily_revenue,
     * deleting an old completed order must not erase
     * the restaurant's historical revenue.
     */
    @Transactional
    public void deleteCompletedOrder(
            UUID orderId
    ) {

        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Order not found: "
                                                        + orderId
                                        )
                        );


        if (
                order.getOrderStatus()
                        != OrderStatus.COMPLETED
        ) {

            throw new IllegalStateException(
                    "Only completed orders can be permanently deleted."
            );
        }


        orderRepository.delete(order);


        log.info(
                "Deleted completed order {}. "
                        + "Historical revenue was preserved.",
                orderId
        );
    }
}