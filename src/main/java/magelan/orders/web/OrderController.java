package magelan.orders.web;

import lombok.RequiredArgsConstructor;
import magelan.orders.order.model.Order;
import magelan.orders.order.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/add-item")
    public String addItem(@RequestParam UUID productId) {
        orderService.addProductToPendingOrder(productId);
        return "redirect:/orders/pending";
    }

    @GetMapping("/pending")
    public String pendingOrders(Model model) {
        Order pendingOrder = orderService.getPendingOrder();
        model.addAttribute("pendingOrder", pendingOrder);
        return "orders-pending";
    }

    @PostMapping("/items/{id}/remove")
    public String removeItem(@PathVariable UUID id) {
        orderService.removeItem(id);
        return "redirect:/orders/pending";
    }

    @PostMapping("/complete")
    public String completeOrder() {
        orderService.completePendingOrder();
        return "redirect:/orders/completed";
    }

    @GetMapping("/completed")
    public String completedOrders(Model model) {
        model.addAttribute("completedOrders", orderService.getCompletedOrders());
        return "orders-completed";
    }
}