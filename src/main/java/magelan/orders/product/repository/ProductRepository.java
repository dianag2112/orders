package magelan.orders.product.repository;

import magelan.orders.product.model.Product;
import magelan.orders.product.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByCategoryOrderByNameAsc(ProductCategory category);
}
