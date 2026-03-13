package magelan.orders.order.repository;

import magelan.orders.order.model.Order;
import magelan.orders.order.model.OrderItem;
import magelan.orders.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    Optional<OrderItem> findByOrderAndProduct(Order order, Product product);
}