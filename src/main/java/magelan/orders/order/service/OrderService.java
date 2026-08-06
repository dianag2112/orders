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

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createPendingOrder(String orderName) {
        Order order = Order.builder()
                .orderName(orderName)
                .orderStatus(OrderStatus.PENDING)
                .amount(BigDecimal.ZERO)
                .createdOn(LocalDateTime.now())
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
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
    }

    @Transactional
    public void addProductToOrder(UUID orderId, UUID productId) {
        Order order = getById(orderId);

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Items can only be added to pending orders.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        OrderItem item = orderItemRepository.findByOrderAndProduct(order, product)
                .orElseGet(() -> {
                    OrderItem newItem = OrderItem.builder()
                            .order(order)
                            .product(product)
                            .quantity(0)
                            .unitPrice(product.getPrice())
                            .createdOn(LocalDateTime.now())
                            .build();

                    order.getItems().add(newItem);
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + 1);
        item.setUnitPrice(product.getPrice());

        recalculateAmount(order);
        orderRepository.save(order);
    }

    @Transactional
    public void removeItem(UUID itemId) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Order item not found: " + itemId));

        Order order = item.getOrder();
        order.getItems().remove(item);
        orderItemRepository.delete(item);

        recalculateAmount(order);
        orderRepository.save(order);
    }

    @Transactional
    public void completeOrder(UUID orderId) {
        Order order = getById(orderId);
        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setCompletedOn(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(UUID orderId) {
        Order order = getById(orderId);
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    private void recalculateAmount(Order order) {
        BigDecimal total = order.getItems().stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setAmount(total);
    }

    @Transactional
    public Order increaseItemQuantity(UUID itemId) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Order item not found"));

        item.setQuantity(item.getQuantity() + 1);
        orderItemRepository.save(item);

        Order order = item.getOrder();
        recalculateAmount(order);
        orderRepository.save(order);

        return orderRepository.findById(order.getId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    @Transactional
    public Order decreaseItemQuantity(UUID itemId) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Order item not found"));

        Order order = item.getOrder();

        if (item.getQuantity() <= 1) {
            order.getItems().remove(item);
            orderItemRepository.delete(item);

            recalculateAmount(order);
            orderRepository.save(order);

            return orderRepository.findById(order.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        }

        item.setQuantity(item.getQuantity() - 1);
        orderItemRepository.save(item);

        recalculateAmount(order);
        orderRepository.save(order);

        return orderRepository.findById(order.getId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    private OrderCardResponse mapToOrderCardResponse(Order order) {
        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ))
                .toList();

        int totalItems = order.getItems()
                .stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();

        return new OrderCardResponse(
                order.getId(),
                order.getOrderName(),
                order.getAmount(),
                totalItems,
                items
        );
    }

    @Transactional
    public OrderCardResponse removeItemAndReturnOrderCard(UUID itemId) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        Order order = item.getOrder();

        order.getItems().remove(item);
        orderItemRepository.delete(item);

        recalculateAmount(order);
        orderRepository.save(order);

        return mapToOrderCardResponse(order);
    }

    @Transactional
    public OrderCardResponse increaseItemQuantityAndReturnOrderCard(UUID itemId) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        item.setQuantity(item.getQuantity() + 1);
        orderItemRepository.save(item);

        Order order = item.getOrder();
        recalculateAmount(order);
        orderRepository.save(order);

        return mapToOrderCardResponse(order);
    }

    @Transactional
    public OrderCardResponse decreaseItemQuantityAndReturnOrderCard(UUID itemId) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        Order order = item.getOrder();

        if (item.getQuantity() <= 1) {
            order.getItems().remove(item);
            orderItemRepository.delete(item);
        } else {
            item.setQuantity(item.getQuantity() - 1);
            orderItemRepository.save(item);
        }

        recalculateAmount(order);
        orderRepository.save(order);

        return mapToOrderCardResponse(order);
    }

    @Transactional(readOnly = true)
    public OrderCardResponse getPendingOrderCard(UUID orderId) {
        Order order = getById(orderId);

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending orders can be opened from the menu."
            );
        }

        return mapToOrderCardResponse(order);
    }

    @Transactional(readOnly = true)
    public Order getCompletedOrderById(UUID orderId) {
        Order order = getById(orderId);

        if (order.getOrderStatus() != OrderStatus.COMPLETED) {
            throw new IllegalStateException(
                    "A receipt can only be generated for a completed order."
            );
        }

        return order;
    }

    @Transactional
    public void deleteCompletedOrder(UUID orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Order not found: " + orderId
                        )
                );

        if (order.getOrderStatus() != OrderStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Only completed orders can be permanently deleted."
            );
        }

        orderRepository.delete(order);
    }
}