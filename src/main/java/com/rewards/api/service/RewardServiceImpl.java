package com.rewards.api.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rewards.api.dto.MonthlyReward;
import com.rewards.api.dto.RewardSummaryResponse;
import com.rewards.api.entity.Customer;
import com.rewards.api.entity.Transaction;
import com.rewards.api.exception.InvalidDateRangeException;
import com.rewards.api.exception.ResourceNotFoundException;
import com.rewards.api.repository.CustomerRepository;
import com.rewards.api.repository.TransactionRepository;
import com.rewards.api.util.RewardCalculator;

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
    public List<RewardSummaryResponse> getAllCustomerRewards(
            LocalDate startDate,
            LocalDate endDate) {

        validateDateRange(startDate, endDate);
        
        List<Transaction> allTransactions = transactionRepository.findAllWithCustomerByTransactionDateBetween(startDate, endDate);
        
        Map<Customer, List<Transaction>> transactionsByCustomer = allTransactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCustomer,
                        () -> new TreeMap<>(Comparator.comparing(Customer::getId)),
                        Collectors.toList()
                ));

        return transactionsByCustomer.entrySet().stream()
                .map(entry -> buildRewardSummary(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private RewardSummaryResponse buildRewardSummary(
            Customer customer,
            List<Transaction> transactions) {

        Map<YearMonth, Integer> monthlyRewardsMap = new TreeMap<>();

        int totalRewards = 0;

        for (Transaction transaction : transactions) {

            int points = rewardCalculator.calculatePoints(
                    transaction.getAmount());

            totalRewards += points;
            YearMonth transactionYearAndMonth = YearMonth.of(transaction.getTransactionDate().getYear(), transaction.getTransactionDate().getMonth()) ;
            
            monthlyRewardsMap.merge(
            		transactionYearAndMonth,
                    points,
                    Integer::sum);
        }

        List<MonthlyReward> monthlyRewards =
                monthlyRewardsMap.entrySet()
                        .stream()
                        .map(entry ->
                                new MonthlyReward(
                                        entry.getKey().toString(),
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