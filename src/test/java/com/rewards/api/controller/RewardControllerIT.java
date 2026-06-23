package com.rewards.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(
	    locations = "classpath:application-test.properties"
	)
@AutoConfigureTestDatabase(
	    replace = AutoConfigureTestDatabase.Replace.ANY
	)
@Sql(
	    scripts = "/test-data.sql",
	    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
	)
class RewardControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnRewardsByCustomerId()
            throws Exception {

        mockMvc.perform(
                get("/api/rewards/customers/1")
                        .param("startDate","2026-01-01")
                        .param("endDate","2026-03-31"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.customerId")
                                .exists());
    }

    @Test
    void shouldReturn404WhenCustomerNotFound()
            throws Exception {

        mockMvc.perform(
                get("/api/rewards/customers/999")
                        .param("startDate","2026-01-01")
                        .param("endDate","2026-03-31"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400ForInvalidDateRange()
            throws Exception {

        mockMvc.perform(
                get("/api/rewards/customers/1")
                        .param("startDate","2026-03-31")
                        .param("endDate","2026-01-01"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnAllCustomersRewards()
            throws Exception {

        mockMvc.perform(
                get("/api/rewards")
                        .param("startDate","2026-01-01")
                        .param("endDate","2026-03-31"))
                .andExpect(status().isOk());
    }
}