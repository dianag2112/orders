package magelan.orders.menu.service;

import lombok.RequiredArgsConstructor;
import magelan.orders.product.model.Product;
import magelan.orders.product.service.ProductService;
import magelan.orders.web.dto.MenuSectionView;
import magelan.orders.web.dto.MenuSubcategoryView;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final ProductService productService;

    public List<MenuSectionView> getMenuSections() {
        List<Product> allProducts = productService.getAll().stream()
                .sorted(
                        Comparator.comparing(Product::getSectionOrder)
                                .thenComparing(p -> p.getSubcategoryOrder() == null ? -1 : p.getSubcategoryOrder())
                                .thenComparing(Product::getItemOrder)
                )
                .toList();

        return buildSections(allProducts);
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