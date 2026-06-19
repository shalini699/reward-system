package com.rewards.rewards_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequest {

    @NotNull
    private Long customerId;

    @Positive
    private BigDecimal amount;

    @NotNull
    private LocalDate transactionDate;
}