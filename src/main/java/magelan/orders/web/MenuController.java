package magelan.orders.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import magelan.orders.product.model.Product;
import magelan.orders.product.model.ProductCategory;
import magelan.orders.product.service.ProductService;
import magelan.orders.web.dto.ProductForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final ProductService productService;

    @GetMapping
    public String getMenu(Model model) {
        if (!model.containsAttribute("productForm")) {
            model.addAttribute("productForm", new ProductForm());
        }

        model.addAttribute("categories", ProductCategory.values());
        loadCategories(model);

        return "menu";
    }

    @PostMapping("/products")
    public String addProduct(@Valid @ModelAttribute("productForm") ProductForm productForm,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productForm", bindingResult);
            redirectAttributes.addFlashAttribute("productForm", productForm);
            return "redirect:/menu";
        }

        productService.create(productForm);
        redirectAttributes.addFlashAttribute("menuMessage", "Item added.");
        return "redirect:/menu#" + productForm.getCategory().name();
    }

    @PostMapping("/products/{id}/edit")
    public String editProduct(@PathVariable UUID id,
                              @RequestParam String name,
                              @RequestParam BigDecimal price,
                              @RequestParam ProductCategory category,
                              RedirectAttributes redirectAttributes) {

        Product product = productService.getById(id);

        ProductForm form = productService.toForm(product);
        form.setName(name);
        form.setPrice(price);
        form.setCategory(category);

        productService.update(id, form);

        redirectAttributes.addFlashAttribute("menuMessage", "Item updated.");
        return "redirect:/menu#" + category.name();
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable UUID id,
                                RedirectAttributes redirectAttributes) {

        Product p = productService.getById(id);
        ProductCategory category = p.getCategory();

        productService.delete(id);

        redirectAttributes.addFlashAttribute("menuMessage", "Item deleted.");
        return "redirect:/menu#" + category.name();
    }

    private void loadCategories(Model model) {
        model.addAttribute("starters", productService.getByCategory(ProductCategory.STARTER));
        model.addAttribute("mains", productService.getByCategory(ProductCategory.MAIN));
        model.addAttribute("desserts", productService.getByCategory(ProductCategory.DESSERT));
        model.addAttribute("drinks", productService.getByCategory(ProductCategory.DRINK));
    }
}
