package magelan.orders.revenue.repository;

import magelan.orders.revenue.model.DailyRevenue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyRevenueRepository
        extends JpaRepository<DailyRevenue, Long> {

    Optional<DailyRevenue> findByBusinessDate(
            LocalDate businessDate
    );

    List<DailyRevenue>
    findAllByBusinessDateBetweenOrderByBusinessDateAsc(
            LocalDate startDate,
            LocalDate endDate
    );

    List<DailyRevenue>
    findAllByOrderByBusinessDateDesc();
}