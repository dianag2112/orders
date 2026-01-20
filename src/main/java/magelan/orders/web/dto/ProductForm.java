package magelan.orders.web.dto;

import lombok.*;
import magelan.orders.product.model.ProductCategory;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductForm {

    private String id;

    private String name;

    private BigDecimal price;

    private ProductCategory category;
}