package magelan.orders.revenue.dto;

import java.math.BigDecimal;

public record MonthlyRevenueSummary(
        int year,
        int month,
        BigDecimal totalRevenue,
        int completedOrders
) {
}