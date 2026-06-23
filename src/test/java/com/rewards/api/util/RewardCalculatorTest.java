package com.rewards.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class RewardCalculatorTest {

    private final RewardCalculator calculator =
            new RewardCalculator();

    @Test
    void shouldReturnZeroForAmountLessThan50() {

        assertEquals(0,
                calculator.calculatePoints(BigDecimal.valueOf(40.0)));
    }

    @Test
    void shouldReturnZeroForAmountEqualTo50() {

        assertEquals(0,
                calculator.calculatePoints(BigDecimal.valueOf(50.0)));
    }

    @Test
    void shouldCalculatePointsBetween50And100() {

        assertEquals(25,
                calculator.calculatePoints(BigDecimal.valueOf(75.0)));
    }

    @Test
    void shouldCalculatePointsFor100Dollars() {

        assertEquals(50,
                calculator.calculatePoints(BigDecimal.valueOf(100.0)));
    }

    @Test
    void shouldCalculatePointsFor120Dollars() {

        assertEquals(90,
                calculator.calculatePoints(BigDecimal.valueOf(120.0)));
    }

    @Test
    void shouldCalculatePointsFor200Dollars() {

        assertEquals(250,
                calculator.calculatePoints(BigDecimal.valueOf(200.0)));
    }
}