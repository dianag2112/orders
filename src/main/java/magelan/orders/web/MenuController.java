package magelan.orders.web;

import lombok.RequiredArgsConstructor;
import magelan.orders.product.model.Product;
import magelan.orders.product.service.ProductService;
import magelan.orders.web.dto.MenuSectionView;
import magelan.orders.web.dto.MenuSubcategoryView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final ProductService productService;

    @GetMapping("/menu")
    public String getMenu(Model model) {
        List<Product> allProducts = productService.getAll().stream()
                .sorted(
                        Comparator.comparing(Product::getSectionOrder)
                                .thenComparing(p -> p.getSubcategoryOrder() == null ? -1 : p.getSubcategoryOrder())
                                .thenComparing(Product::getItemOrder)
                )
                .toList();

        model.addAttribute("menuSections", buildSections(allProducts));
        return "menu";
    }

    private List<MenuSectionView> buildSections(List<Product> products) {
        LinkedHashMap<String, List<Product>> groupedBySection = new LinkedHashMap<>();

        for (Product product : products) {
            groupedBySection
                    .computeIfAbsent(product.getSectionTitle(), key -> new ArrayList<>())
                    .add(product);
        }

        List<MenuSectionView> result = new ArrayList<>();

        for (Map.Entry<String, List<Product>> sectionEntry : groupedBySection.entrySet()) {
            List<Product> sectionProducts = sectionEntry.getValue();

            List<Product> directProducts = sectionProducts.stream()
                    .filter(p -> p.getSubcategoryTitle() == null || p.getSubcategoryTitle().isBlank())
                    .sorted(Comparator.comparing(Product::getItemOrder))
                    .toList();

            LinkedHashMap<String, List<Product>> groupedBySubcategory = new LinkedHashMap<>();

            sectionProducts.stream()
                    .filter(p -> p.getSubcategoryTitle() != null && !p.getSubcategoryTitle().isBlank())
                    .sorted(
                            Comparator.comparing((Product p) -> p.getSubcategoryOrder() == null ? Integer.MAX_VALUE : p.getSubcategoryOrder())
                                    .thenComparing(Product::getItemOrder)
                    )
                    .forEach(product ->
                            groupedBySubcategory
                                    .computeIfAbsent(product.getSubcategoryTitle(), key -> new ArrayList<>())
                                    .add(product)
                    );

            List<MenuSubcategoryView> subcategories = groupedBySubcategory.entrySet().stream()
                    .map(entry -> new MenuSubcategoryView(entry.getKey(), entry.getValue()))
                    .toList();

            result.add(new MenuSectionView(sectionEntry.getKey(), directProducts, subcategories));
        }

        return result;
    }
}