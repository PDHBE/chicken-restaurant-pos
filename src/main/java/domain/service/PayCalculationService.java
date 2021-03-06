package domain.service;

import domain.model.Order;
import domain.model.PaymentType;

import java.util.List;

public class PayCalculationService {
    private static final int CHICKEN_DISCOUNT_CRITERIA = 10;
    private static final int CHICKEN_DISCOUNT_AMOUNT = 10000;
    private static final double CASH_DISCOUNT_RATE = 0.05;

    public static int calculate(List<Order> orders, PaymentType paymentType) {
        int payment = getRawPayment(orders);

        int sumOfChickenMenus = getSumOfChickenMenus(orders);
        if (sumOfChickenMenus >= CHICKEN_DISCOUNT_CRITERIA) {
            payment -= (sumOfChickenMenus / CHICKEN_DISCOUNT_CRITERIA) * CHICKEN_DISCOUNT_AMOUNT;
        }

        if (paymentType.isCash()) {
            payment -= payment * CASH_DISCOUNT_RATE;
        }

        return payment;
    }

    private static int getRawPayment(List<Order> orders) {
        int payment = 0;
        for (Order order : orders) {
            payment += order.calculateAmount();
        }
        return payment;
    }

    private static int getSumOfChickenMenus(List<Order> orders) {
        int sumOfChickenMenus = 0;
        for (Order order : orders) {
            if (order.isChicken()) {
                sumOfChickenMenus += order.getQuantity();
            }
        }
        return sumOfChickenMenus;
    }
}
