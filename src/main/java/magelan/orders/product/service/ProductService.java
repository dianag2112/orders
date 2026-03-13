package magelan.orders.product.service;

import lombok.extern.slf4j.Slf4j;
import magelan.orders.product.model.Product;
import magelan.orders.product.model.ProductCategory;
import magelan.orders.product.repository.ProductRepository;
import magelan.orders.web.dto.ProductForm;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAll() {
        return productRepository.findAllByOrderBySectionOrderAscSubcategoryOrderAscItemOrderAsc();
    }

    public List<Product> getByCategory(ProductCategory category) {
        return productRepository.findAllByCategoryOrderBySectionOrderAscSubcategoryOrderAscItemOrderAsc(category);
    }

    public Product getById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with ID [%s] not found.".formatted(id)));
    }

    public Product create(ProductForm form) {
        LocalDateTime now = LocalDateTime.now();

        Product product = Product.builder()
                .name(form.getName())
                .description(form.getDescription())
                .price(form.getPrice())
                .category(form.getCategory())
                .sectionOrder(form.getSectionOrder())
                .sectionTitle(form.getSectionTitle())
                .subcategoryOrder(form.getSubcategoryOrder())
                .subcategoryTitle(form.getSubcategoryTitle())
                .itemOrder(form.getItemOrder())
                .createdOn(now)
                .updatedOn(now)
                .build();

        return productRepository.save(product);
    }

    public Product update(UUID id, ProductForm form) {
        Product product = getById(id);

        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setCategory(form.getCategory());
        product.setSectionOrder(form.getSectionOrder());
        product.setSectionTitle(form.getSectionTitle());
        product.setSubcategoryOrder(form.getSubcategoryOrder());
        product.setSubcategoryTitle(form.getSubcategoryTitle());
        product.setItemOrder(form.getItemOrder());
        product.setUpdatedOn(LocalDateTime.now());

        return productRepository.save(product);
    }

    public void delete(UUID id) {
        productRepository.deleteById(id);
    }
}