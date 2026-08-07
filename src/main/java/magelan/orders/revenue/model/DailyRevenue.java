package magelan.orders.revenue.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "daily_revenue",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_daily_revenue_business_date",
                        columnNames = "business_date"
                )
        }
)
public class DailyRevenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "business_date",
            nullable = false,
            unique = true
    )
    private LocalDate businessDate;

    @Column(
            name = "total_revenue",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal totalRevenue;

    @Column(
            name = "completed_orders",
            nullable = false
    )
    private Integer completedOrders;

    @Column(
            name = "created_on",
            nullable = false
    )
    private LocalDateTime createdOn;

    @Column(
            name = "updated_on",
            nullable = false
    )
    private LocalDateTime updatedOn;
}