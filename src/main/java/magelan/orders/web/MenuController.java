package magelan.orders.web;

import lombok.RequiredArgsConstructor;
import magelan.orders.product.model.ProductCategory;
import magelan.orders.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final ProductService productService;

    @GetMapping
    public String getMenu(Model model) {
        model.addAttribute("starters", productService.getByCategory(ProductCategory.STARTER));
        model.addAttribute("mains", productService.getByCategory(ProductCategory.MAIN));
        model.addAttribute("desserts", productService.getByCategory(ProductCategory.DESSERT));
        model.addAttribute("drinks", productService.getByCategory(ProductCategory.DRINK));

        return "menu";
    }
}
