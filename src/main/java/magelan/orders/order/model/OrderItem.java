package magelan.orders.order.model;

import jakarta.persistence.*;
import lombok.*;
import magelan.orders.product.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    private Order order;

    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER
    )
    private Product product;

    /*
     * Quantity supports half portions:
     *
     * 0.5
     * 1.0
     * 1.5
     * 2.0
     * etc.
     */
    @Column(
            nullable = false,
            precision = 10,
            scale = 1
    )
    private BigDecimal quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private LocalDateTime createdOn;


    public BigDecimal getTotalPrice() {

        return unitPrice.multiply(
                quantity
        );
    }
}