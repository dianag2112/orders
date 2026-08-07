package magelan.orders.web;

import lombok.RequiredArgsConstructor;
import magelan.orders.revenue.service.DailyRevenueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RevenueStatisticsController {

    private final DailyRevenueService
            dailyRevenueService;


    @GetMapping("/orders/statistics")
    public String statistics(
            Model model
    ) {

        model.addAttribute(
                "currentBusinessDate",
                dailyRevenueService
                        .getCurrentBusinessDate()
        );


        /*
         * Current business day
         */
        model.addAttribute(
                "todayRevenue",
                dailyRevenueService
                        .getCurrentBusinessDayRevenue()
        );

        model.addAttribute(
                "todayCompletedOrders",
                dailyRevenueService
                        .getCurrentBusinessDayCompletedOrders()
        );


        /*
         * Current week
         */
        model.addAttribute(
                "weekRevenue",
                dailyRevenueService
                        .getCurrentWeekRevenue()
        );

        model.addAttribute(
                "weekCompletedOrders",
                dailyRevenueService
                        .getCurrentWeekCompletedOrders()
        );


        /*
         * Current month
         */
        model.addAttribute(
                "monthRevenue",
                dailyRevenueService
                        .getCurrentMonthRevenue()
        );

        model.addAttribute(
                "monthCompletedOrders",
                dailyRevenueService
                        .getCurrentMonthCompletedOrders()
        );


        /*
         * Complete daily history
         */
        model.addAttribute(
                "dailyRevenueHistory",
                dailyRevenueService
                        .getHistory()
        );


        return "orders-statistics";
    }
}