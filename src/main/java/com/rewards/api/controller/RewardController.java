package com.rewards.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rewards.api.dto.RewardSummaryResponse;
import com.rewards.api.service.RewardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Rewards API", description = "APIs for Customer reward calculation")
@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;
    
    @Operation(summary = "Get rewards earned by customer by customer ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rewards retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found"),
        @ApiResponse(responseCode = "400", description = "Invalid date range")
    })
    @GetMapping("/customers/{customerId}")
    public RewardSummaryResponse getRewardsById(
            @PathVariable Long customerId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate){

        return rewardService
                .getRewardsByCustomerId(
                        customerId,
                        startDate,
                        endDate);
    }
    
    @Operation(summary = "Get reward points earned for all customers",
            description = "Returns reward points earned for each customer per month and total based on specified date range."
    		)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rewards retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date range")
    })
    @GetMapping
    public List<RewardSummaryResponse> getAllRewards(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return rewardService.getAllCustomerRewards(
                startDate,
                endDate);
    }
}