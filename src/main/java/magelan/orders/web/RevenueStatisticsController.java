package magelan.orders.web;

import lombok.RequiredArgsConstructor;
import magelan.orders.revenue.service.DailyRevenueService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

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
         * Complete month-by-month history
         */
        model.addAttribute(
                "monthlyRevenueHistory",
                dailyRevenueService
                        .getMonthlyHistory()
        );


        /*
         * Complete day-by-day history
         */
        model.addAttribute(
                "dailyRevenueHistory",
                dailyRevenueService
                        .getHistory()
        );


        return "orders-statistics";
    }


    @PostMapping(
            "/orders/statistics/{businessDate}/delete"
    )
    public String deleteRevenueDay(
            @PathVariable
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate businessDate
    ) {

        dailyRevenueService
                .deleteRevenueDay(
                        businessDate
                );

        return "redirect:/orders/statistics";
    }
}