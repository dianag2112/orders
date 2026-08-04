package magelan.orders.web;

import lombok.RequiredArgsConstructor;
import magelan.orders.menu.service.MenuService;
import magelan.orders.order.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final OrderService orderService;

    @GetMapping("/menu")
    public String getMenu(
            @RequestParam(required = false) UUID orderId,
            Model model
    ) {
        model.addAttribute("menuSections", menuService.getMenuSections());
        model.addAttribute("pendingOrders", orderService.getPendingOrders());

        // Used when arriving from the dashboard's "Add Items" button
        model.addAttribute("orderId", orderId);

        return "menu";
    }
}