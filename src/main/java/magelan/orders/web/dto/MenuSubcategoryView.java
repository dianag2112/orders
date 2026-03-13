package magelan.orders.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import magelan.orders.product.model.Product;

import java.util.List;

@Getter
@AllArgsConstructor
public class MenuSubcategoryView {
    private String title;
    private List<Product> products;
}