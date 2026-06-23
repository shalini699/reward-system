package com.rewards.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RewardSummaryResponse {

    private Long customerId;

    private String customerName;

    private List<MonthlyReward> monthlyRewards;

    private Integer totalRewards;
}