package magelan.orders.web;

import lombok.RequiredArgsConstructor;
import magelan.orders.order.dto.OrderCardResponse;
import magelan.orders.order.service.OrderService;
import magelan.orders.revenue.service.DailyRevenueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final DailyRevenueService
            dailyRevenueService;


    @GetMapping("/pending")
    public String pendingDashboard(
            Model model
    ) {

        model.addAttribute(
                "pendingOrders",
                orderService.getPendingOrders()
        );

        return "orders-pending-dashboard";
    }


    /*
     * Completed Orders page.
     *
     * Besides the order history, this now sends
     * the current business-day revenue to the page.
     */
    @GetMapping("/completed")
    public String completedOrders(
            Model model
    ) {

        model.addAttribute(
                "completedOrders",
                orderService.getCompletedOrders()
        );


        model.addAttribute(
                "currentBusinessDate",
                dailyRevenueService
                        .getCurrentBusinessDate()
        );


        model.addAttribute(
                "todayRevenue",
                dailyRevenueService
                        .getCurrentBusinessDayRevenue()
        );


        model.addAttribute(
                "todayCompletedOrders",
                dailyRevenueService
                        .getCurrentBusinessDayCompletedOrders()
        );


        return "orders-completed";
    }


    @PostMapping("/new")
    public String createOrder(
            @RequestParam String orderName
    ) {

        var order =
                orderService
                        .createPendingOrder(
                                orderName
                        );

        return "redirect:/menu?orderId="
                + order.getId();
    }


    @PostMapping("/{orderId}/add-item")
    public String addItem(
            @PathVariable UUID orderId,
            @RequestParam UUID productId
    ) {

        orderService
                .addProductToOrder(
                        orderId,
                        productId
                );

        return "redirect:/menu?orderId="
                + orderId;
    }


    @PostMapping("/items/{itemId}/remove")
    public String removeItem(
            @PathVariable UUID itemId,
            @RequestParam UUID orderId
    ) {

        orderService
                .removeItem(
                        itemId
                );

        return "redirect:/orders/pending";
    }


    @PostMapping("/{orderId}/complete")
    public String completeOrder(
            @PathVariable UUID orderId
    ) {

        orderService
                .completeOrder(
                        orderId
                );

        return "redirect:/orders/pending";
    }


    @PostMapping("/{orderId}/cancel")
    public String cancelOrder(
            @PathVariable UUID orderId
    ) {

        orderService
                .cancelOrder(
                        orderId
                );

        return "redirect:/orders/pending";
    }


    @PostMapping("/items/{itemId}/increase")
    @ResponseBody
    public OrderCardResponse increaseItemQuantity(
            @PathVariable UUID itemId
    ) {

        return orderService
                .increaseItemQuantityAndReturnOrderCard(
                        itemId
                );
    }


    @PostMapping("/items/{itemId}/decrease")
    @ResponseBody
    public OrderCardResponse decreaseItemQuantity(
            @PathVariable UUID itemId
    ) {

        return orderService
                .decreaseItemQuantityAndReturnOrderCard(
                        itemId
                );
    }


    @PostMapping("/items/{itemId}/remove-ajax")
    @ResponseBody
    public OrderCardResponse removeItemAjax(
            @PathVariable UUID itemId
    ) {

        return orderService
                .removeItemAndReturnOrderCard(
                        itemId
                );
    }


    public record OrderItemRowResponse(
            UUID itemId,
            String productName,
            int quantity,
            String unitPrice,
            String lineTotal
    ) {
    }


    @GetMapping("/{orderId}/card")
    @ResponseBody
    public OrderCardResponse getOrderCard(
            @PathVariable UUID orderId
    ) {

        return orderService
                .getPendingOrderCard(
                        orderId
                );
    }


    @PostMapping("/{orderId}/add-item-ajax")
    @ResponseBody
    public OrderCardResponse addItemAjax(
            @PathVariable UUID orderId,
            @RequestParam UUID productId
    ) {

        orderService
                .addProductToOrder(
                        orderId,
                        productId
                );

        return orderService
                .getPendingOrderCard(
                        orderId
                );
    }


    /*
     * Removes the completed order from the order
     * history but does NOT remove its recorded revenue.
     */
    @PostMapping("/{orderId}/delete-completed")
    public String deleteCompletedOrder(
            @PathVariable UUID orderId
    ) {

        orderService
                .deleteCompletedOrder(
                        orderId
                );

        return "redirect:/orders/completed";
    }
}