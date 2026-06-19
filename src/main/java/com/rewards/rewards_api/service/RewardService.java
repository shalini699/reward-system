package com.rewards.rewards_api.service;

import java.time.LocalDate;
import java.util.List;

import com.rewards.rewards_api.dto.RewardSummaryResponse;

public interface RewardService {

    RewardSummaryResponse getRewardsByCustomerId(
            Long customerId,
            LocalDate startDate,
            LocalDate endDate);

    RewardSummaryResponse getRewardsByCustomerName(
            String customerName,
            LocalDate startDate,
            LocalDate endDate);

    List<RewardSummaryResponse> getAllCustomerRewards(
            LocalDate startDate,
            LocalDate endDate);
}