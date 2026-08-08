package magelan.orders.revenue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import magelan.orders.order.model.Order;
import magelan.orders.order.model.OrderStatus;
import magelan.orders.order.repository.OrderRepository;
import magelan.orders.revenue.service.DailyRevenueService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "magelan.revenue.backfill-enabled",
        havingValue = "true"
)
@RequiredArgsConstructor
public class RevenueHistoryInitializer
        implements CommandLineRunner {

    private final OrderRepository
            orderRepository;

    private final DailyRevenueService
            dailyRevenueService;


    @Override
    public void run(
            String... args
    ) {

        /*
         * This is deliberately run only once.
         *
         * After revenue history exists, we never rebuild it
         * from the Order table because completed orders may
         * later be permanently deleted.
         */
        if (
                dailyRevenueService
                        .hasRevenueHistory()
        ) {
            log.info(
                    "Revenue history already exists. "
                            + "Skipping revenue backfill."
            );

            return;
        }


        List<Order> completedOrders =
                orderRepository
                        .findAll()
                        .stream()
                        .filter(
                                order ->
                                        order.getOrderStatus()
                                                == OrderStatus.COMPLETED
                        )
                        .toList();


        log.info(
                "Backfilling revenue history from {} completed orders...",
                completedOrders.size()
        );


        for (
                Order order
                : completedOrders
        ) {

            LocalDateTime completedAt =
                    order.getCompletedOn() != null
                            ? order.getCompletedOn()
                            : order.getCreatedOn();


            dailyRevenueService
                    .addHistoricalCompletedOrder(
                            order.getAmount(),
                            completedAt
                    );
        }


        log.info(
                "Revenue history backfill completed."
        );
    }
}