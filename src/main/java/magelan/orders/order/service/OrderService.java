package magelan.orders.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public void addProductToPendingOrder(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        Order order = getOrCreatePendingOrder();

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

        recalculateOrderAmount(order);
        orderRepository.save(order);

        log.info("Added product {} to pending order {}", product.getId(), order.getId());
    }

    @Transactional(readOnly = true)
    public Order getPendingOrder() {
        return orderRepository.findFirstByOrderStatusOrderByCreatedOnDesc(OrderStatus.PENDING)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Order> getCompletedOrders() {
        return orderRepository.findAllByOrderStatusOrderByCreatedOnDesc(OrderStatus.COMPLETED);
    }

    @Transactional
    public void completePendingOrder() {
        Order order = getPendingOrder();
        if (order == null || order.getItems().isEmpty()) {
            return;
        }

        order.setOrderStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
    }

    @Transactional
    public void removeItem(UUID orderItemId) {
        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("Order item not found: " + orderItemId));

        Order order = item.getOrder();

        order.getItems().remove(item);
        orderItemRepository.delete(item);

        recalculateOrderAmount(order);
        orderRepository.save(order);
    }

    private Order getOrCreatePendingOrder() {
        return orderRepository.findFirstByOrderStatusOrderByCreatedOnDesc(OrderStatus.PENDING)
                .orElseGet(() -> orderRepository.save(
                        Order.builder()
                                .tableName("1")
                                .orderStatus(OrderStatus.PENDING)
                                .amount(BigDecimal.ZERO)
                                .createdOn(LocalDateTime.now())
                                .build()
                ));
    }

    private void recalculateOrderAmount(Order order) {
        BigDecimal amount = order.getItems().stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setAmount(amount);
    }
}