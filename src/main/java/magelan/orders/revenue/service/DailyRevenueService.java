package magelan.orders.revenue.service;

import lombok.RequiredArgsConstructor;
import magelan.orders.revenue.model.DailyRevenue;
import magelan.orders.revenue.repository.DailyRevenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyRevenueService {

    /*
     * Magelan works according to Bulgarian local time,
     * regardless of the timezone used by Render.
     */
    private static final ZoneId BUSINESS_ZONE =
            ZoneId.of("Europe/Sofia");

    /*
     * A new business day starts at 06:00.
     *
     * Example:
     *
     * 07 Aug 18:00 -> business date 07 Aug
     * 08 Aug 02:30 -> business date 07 Aug
     * 08 Aug 05:59 -> business date 07 Aug
     * 08 Aug 06:00 -> business date 08 Aug
     */
    private static final LocalTime BUSINESS_DAY_START =
            LocalTime.of(6, 0);

    private final DailyRevenueRepository
            dailyRevenueRepository;


    public LocalDateTime getCurrentSofiaDateTime() {
        return LocalDateTime.now(
                BUSINESS_ZONE
        );
    }


    public LocalDate getCurrentBusinessDate() {
        return getBusinessDate(
                getCurrentSofiaDateTime()
        );
    }


    public LocalDate getBusinessDate(
            LocalDateTime dateTime
    ) {
        if (
                dateTime
                        .toLocalTime()
                        .isBefore(
                                BUSINESS_DAY_START
                        )
        ) {
            return dateTime
                    .toLocalDate()
                    .minusDays(1);
        }

        return dateTime.toLocalDate();
    }


    /*
     * Called whenever an order becomes COMPLETED.
     */
    @Transactional
    public void recordCompletedOrder(
            BigDecimal amount,
            LocalDateTime completedAt
    ) {
        if (completedAt == null) {
            completedAt =
                    getCurrentSofiaDateTime();
        }

        if (amount == null) {
            amount =
                    BigDecimal.ZERO;
        }

        LocalDate businessDate =
                getBusinessDate(
                        completedAt
                );

        BigDecimal orderAmount =
                amount;

        DailyRevenue dailyRevenue =
                dailyRevenueRepository
                        .findByBusinessDate(
                                businessDate
                        )
                        .orElseGet(
                                () ->
                                        createDailyRevenue(
                                                businessDate
                                        )
                        );

        dailyRevenue.setTotalRevenue(
                dailyRevenue
                        .getTotalRevenue()
                        .add(
                                orderAmount
                        )
        );

        dailyRevenue.setCompletedOrders(
                dailyRevenue
                        .getCompletedOrders()
                        + 1
        );

        dailyRevenue.setUpdatedOn(
                getCurrentSofiaDateTime()
        );

        dailyRevenueRepository.save(
                dailyRevenue
        );
    }


    /*
     * Used only for initial migration/backfill of
     * completed orders that already existed before
     * the revenue feature was added.
     */
    @Transactional
    public void addHistoricalCompletedOrder(
            BigDecimal amount,
            LocalDateTime completedAt
    ) {
        recordCompletedOrder(
                amount,
                completedAt
        );
    }


    private DailyRevenue createDailyRevenue(
            LocalDate businessDate
    ) {
        LocalDateTime now =
                getCurrentSofiaDateTime();

        return DailyRevenue.builder()
                .businessDate(
                        businessDate
                )
                .totalRevenue(
                        BigDecimal.ZERO
                )
                .completedOrders(
                        0
                )
                .createdOn(
                        now
                )
                .updatedOn(
                        now
                )
                .build();
    }


    public BigDecimal getRevenue(
            LocalDate businessDate
    ) {
        return dailyRevenueRepository
                .findByBusinessDate(
                        businessDate
                )
                .map(
                        DailyRevenue::getTotalRevenue
                )
                .orElse(
                        BigDecimal.ZERO
                );
    }


    public int getCompletedOrders(
            LocalDate businessDate
    ) {
        return dailyRevenueRepository
                .findByBusinessDate(
                        businessDate
                )
                .map(
                        DailyRevenue::getCompletedOrders
                )
                .orElse(
                        0
                );
    }


    public BigDecimal getCurrentBusinessDayRevenue() {
        return getRevenue(
                getCurrentBusinessDate()
        );
    }


    public int getCurrentBusinessDayCompletedOrders() {
        return getCompletedOrders(
                getCurrentBusinessDate()
        );
    }


    /*
     * Week = Monday through Sunday.
     */
    public LocalDate getCurrentWeekStart() {
        return getCurrentBusinessDate()
                .with(
                        TemporalAdjusters
                                .previousOrSame(
                                        DayOfWeek.MONDAY
                                )
                );
    }


    public LocalDate getCurrentWeekEnd() {
        return getCurrentBusinessDate()
                .with(
                        TemporalAdjusters
                                .nextOrSame(
                                        DayOfWeek.SUNDAY
                                )
                );
    }


    public BigDecimal getCurrentWeekRevenue() {
        return sumRevenueBetween(
                getCurrentWeekStart(),
                getCurrentWeekEnd()
        );
    }


    public int getCurrentWeekCompletedOrders() {
        return sumOrdersBetween(
                getCurrentWeekStart(),
                getCurrentWeekEnd()
        );
    }


    public LocalDate getCurrentMonthStart() {
        return getCurrentBusinessDate()
                .withDayOfMonth(1);
    }


    public LocalDate getCurrentMonthEnd() {
        LocalDate currentBusinessDate =
                getCurrentBusinessDate();

        return currentBusinessDate
                .withDayOfMonth(
                        currentBusinessDate
                                .lengthOfMonth()
                );
    }


    public BigDecimal getCurrentMonthRevenue() {
        return sumRevenueBetween(
                getCurrentMonthStart(),
                getCurrentMonthEnd()
        );
    }


    public int getCurrentMonthCompletedOrders() {
        return sumOrdersBetween(
                getCurrentMonthStart(),
                getCurrentMonthEnd()
        );
    }


    public BigDecimal sumRevenueBetween(
            LocalDate startDate,
            LocalDate endDate
    ) {
        return getRevenueBetween(
                startDate,
                endDate
        )
                .stream()
                .map(
                        DailyRevenue::getTotalRevenue
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }


    public int sumOrdersBetween(
            LocalDate startDate,
            LocalDate endDate
    ) {
        return getRevenueBetween(
                startDate,
                endDate
        )
                .stream()
                .mapToInt(
                        DailyRevenue::getCompletedOrders
                )
                .sum();
    }


    public List<DailyRevenue> getRevenueBetween(
            LocalDate startDate,
            LocalDate endDate
    ) {
        return dailyRevenueRepository
                .findAllByBusinessDateBetweenOrderByBusinessDateAsc(
                        startDate,
                        endDate
                );
    }


    public List<DailyRevenue> getHistory() {
        return dailyRevenueRepository
                .findAllByOrderByBusinessDateDesc();
    }

    @Transactional
    public void deleteRevenueDay(
            LocalDate businessDate
    ) {
        if (businessDate == null) {
            throw new IllegalArgumentException(
                    "Business date cannot be null."
            );
        }

        dailyRevenueRepository
                .findByBusinessDate(
                        businessDate
                )
                .ifPresent(
                        dailyRevenueRepository::delete
                );
    }

    public boolean hasRevenueHistory() {
        return dailyRevenueRepository
                .count() > 0;
    }
}