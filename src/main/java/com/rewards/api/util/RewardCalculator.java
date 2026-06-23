package com.rewards.api.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component
public class RewardCalculator {

    public int calculatePoints(BigDecimal amount) {

        // if amount less than or equal to 50 --> no points
        if (amount.compareTo(BigDecimal.valueOf(50)) <= 0) {
            return 0;
        }

        // 1 point for every dollar spent between $50 and $100
        if (amount.compareTo(BigDecimal.valueOf(100)) <= 0) {
            return amount.subtract(BigDecimal.valueOf(50)).intValue();
        }

        // 2 points for every dollar spent over $100
        return 50 + amount
                .subtract(BigDecimal.valueOf(100))
                .multiply(BigDecimal.valueOf(2))
                .setScale(0, RoundingMode.HALF_UP)
                .intValue();
    }
}