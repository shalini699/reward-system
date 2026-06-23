package com.rewards.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rewards.api.dto.RewardSummaryResponse;
import com.rewards.api.entity.Customer;
import com.rewards.api.entity.Transaction;
import com.rewards.api.exception.InvalidDateRangeException;
import com.rewards.api.exception.ResourceNotFoundException;
import com.rewards.api.repository.CustomerRepository;
import com.rewards.api.repository.TransactionRepository;
import com.rewards.api.util.RewardCalculator;

class RewardServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardServiceImpl rewardService;

    private final RewardCalculator rewardCalculator =
            new RewardCalculator();

    @BeforeEach
    void setup() {

        MockitoAnnotations.openMocks(this);

        rewardService = new RewardServiceImpl(
                customerRepository,
                transactionRepository,
                rewardCalculator);
    }

    @Test
    void shouldReturnRewardsForCustomerId() {

        Customer customer = Customer.builder()
                .id(1L)
                .name("John")
                .build();

        Transaction transaction =
                Transaction.builder()
                        .id(1L)
                        .customer(customer)
                        .amount(BigDecimal.valueOf(120.0))
                        .transactionDate(
                                LocalDate.of(2026,1,10))
                        .build();

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(transactionRepository
                .findByCustomer_IdAndTransactionDateBetween(
                        anyLong(),
                        any(),
                        any()))
                .thenReturn(List.of(transaction));

        RewardSummaryResponse response =
                rewardService.getRewardsByCustomerId(
                        1L,
                        LocalDate.of(2026,1,1),
                        LocalDate.of(2026,3,31));

        assertEquals("John",
                response.getCustomerName());

        assertEquals(90,
                response.getTotalRewards());
    }

    @Test
    void shouldThrowCustomerNotFoundForId() {

        when(customerRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> rewardService.getRewardsByCustomerId(
                        99L,
                        LocalDate.now(),
                        LocalDate.now()));
    }


    @Test
    void shouldThrowInvalidDateRangeException() {

        assertThrows(
                InvalidDateRangeException.class,
                () -> rewardService.getRewardsByCustomerId(
                        1L,
                        LocalDate.of(2026,3,31),
                        LocalDate.of(2026,1,1)));
    }

    @Test
    void shouldReturnEmptyRewardsWhenNoTransactionsExist() {

        Customer customer = Customer.builder()
                .id(1L)
                .name("John")
                .build();

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(transactionRepository
                .findByCustomer_IdAndTransactionDateBetween(
                        anyLong(),
                        any(),
                        any()))
                .thenReturn(List.of());

        RewardSummaryResponse response =
                rewardService.getRewardsByCustomerId(
                        1L,
                        LocalDate.of(2026,1,1),
                        LocalDate.of(2026,3,31));

        assertEquals(0,
                response.getTotalRewards());

        assertTrue(
                response.getMonthlyRewards().isEmpty());
    }
}