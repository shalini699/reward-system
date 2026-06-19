package com.rewards.rewards_api.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rewards.rewards_api.dto.MonthlyReward;
import com.rewards.rewards_api.dto.RewardSummaryResponse;
import com.rewards.rewards_api.entity.Customer;
import com.rewards.rewards_api.entity.Transaction;
import com.rewards.rewards_api.exception.InvalidDateRangeException;
import com.rewards.rewards_api.exception.ResourceNotFoundException;
import com.rewards.rewards_api.repository.CustomerRepository;
import com.rewards.rewards_api.repository.TransactionRepository;
import com.rewards.rewards_api.util.RewardCalculator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final RewardCalculator rewardCalculator;

    @Override
    public RewardSummaryResponse getRewardsByCustomerId(
            Long customerId,
            LocalDate startDate,
            LocalDate endDate) {

        validateDateRange(startDate, endDate);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with id: " + customerId));

        List<Transaction> transactions =
                transactionRepository
                        .findByCustomer_IdAndTransactionDateBetween(
                                customerId,
                                startDate,
                                endDate);

        return buildRewardSummary(customer, transactions);
    }

    @Override
    public RewardSummaryResponse getRewardsByCustomerName(
            String customerName,
            LocalDate startDate,
            LocalDate endDate) {

        validateDateRange(startDate, endDate);

        Customer customer = customerRepository
                .findByNameIgnoreCase(customerName)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with name: " + customerName));

        List<Transaction> transactions =
                transactionRepository
                        .findByCustomer_NameIgnoreCaseAndTransactionDateBetween(
                                customerName,
                                startDate,
                                endDate);

        return buildRewardSummary(customer, transactions);
    }

    @Override
    public List<RewardSummaryResponse> getAllCustomerRewards(
            LocalDate startDate,
            LocalDate endDate) {

        validateDateRange(startDate, endDate);

        List<Customer> customers = customerRepository.findAll();

        return customers.stream()
                .map(customer -> {

                    List<Transaction> transactions =
                            transactionRepository
                                    .findByCustomer_IdAndTransactionDateBetween(
                                            customer.getId(),
                                            startDate,
                                            endDate);

                    return buildRewardSummary(customer, transactions);
                })
                .collect(Collectors.toList());
    }

    private RewardSummaryResponse buildRewardSummary(
            Customer customer,
            List<Transaction> transactions) {

        Map<Month, Integer> monthlyRewardsMap = new TreeMap<>();

        int totalRewards = 0;

        for (Transaction transaction : transactions) {

            int points = rewardCalculator.calculatePoints(
                    transaction.getAmount());

            totalRewards += points;

            Month month = transaction.getTransactionDate().getMonth();

            monthlyRewardsMap.merge(
                    month,
                    points,
                    Integer::sum);
        }

        List<MonthlyReward> monthlyRewards =
                monthlyRewardsMap.entrySet()
                        .stream()
                        .map(entry ->
                                new MonthlyReward(
                                        entry.getKey().name(),
                                        entry.getValue()))
                        .toList();

        return RewardSummaryResponse.builder()
                .customerId(customer.getId())
                .customerName(customer.getName())
                .monthlyRewards(monthlyRewards)
                .totalRewards(totalRewards)
                .build();
    }

    private void validateDateRange(
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new InvalidDateRangeException(
                    "Start date and end date are required");
        }

        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException(
                    "Start date cannot be after end date");
        }
    }
}