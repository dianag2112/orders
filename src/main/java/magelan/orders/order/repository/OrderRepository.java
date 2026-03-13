package magelan.orders.order.repository;

import magelan.orders.order.model.Order;
import magelan.orders.order.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findFirstByOrderStatusOrderByCreatedOnDesc(OrderStatus orderStatus);

    List<Order> findAllByOrderStatusOrderByCreatedOnDesc(OrderStatus orderStatus);
}