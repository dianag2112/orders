package magelan.orders.product.repository;

import magelan.orders.product.model.Product;
import magelan.orders.product.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByName(String name);

    List<Product> findAllByOrderBySectionOrderAscSubcategoryOrderAscItemOrderAsc();

    List<Product> findAllByCategoryOrderBySectionOrderAscSubcategoryOrderAscItemOrderAsc(
            ProductCategory category
    );

    @Query("""
            SELECT p
            FROM Product p
            WHERE p.category = :category
              AND p.sectionOrder = :sectionOrder
              AND (
                    (:subcategoryOrder IS NULL AND p.subcategoryOrder IS NULL)
                    OR p.subcategoryOrder = :subcategoryOrder
                  )
              AND p.itemOrder = :itemOrder
            ORDER BY p.createdOn ASC
            """)
    List<Product> findAllByMenuPosition(
            @Param("category")
            ProductCategory category,

            @Param("sectionOrder")
            Integer sectionOrder,

            @Param("subcategoryOrder")
            Integer subcategoryOrder,

            @Param("itemOrder")
            Integer itemOrder
    );
}