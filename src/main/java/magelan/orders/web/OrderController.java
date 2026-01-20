package magelan.orders.web;

import magelan.orders.order.model.Order;
import magelan.orders.order.model.OrderStatus;
import magelan.orders.order.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class OrderController {
    private final OrderRepository orderRepository;
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        List<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/orders/new")
    public String createTestOrder() {
        orderRepository.save(Order.builder()
                .tableName("1")
                .orderStatus(OrderStatus.PENDING)
                .amount(BigDecimal.TEN)
                .createdOn(LocalDateTime.now())
                .items(List.of())
                .build()
        );
        return "redirect:/orders";
    }
}
