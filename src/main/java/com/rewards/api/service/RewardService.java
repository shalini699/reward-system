package com.rewards.api.service;

import java.time.LocalDate;
import java.util.List;

import com.rewards.api.dto.RewardSummaryResponse;

public interface RewardService {

    RewardSummaryResponse getRewardsByCustomerId(
            Long customerId,
            LocalDate startDate,
            LocalDate endDate);

    List<RewardSummaryResponse> getAllCustomerRewards(
            LocalDate startDate,
            LocalDate endDate);
}