package com.rewards.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Retail Rewards API",
                version = "1.0",
                description = "API for calculating customer reward points based on transactions",
                contact = @Contact(
                        name = "Shalini",
                        email = "shalini@xxx.com"
                )
        )
)
public class OpenApiConfig {

    @Bean
    public OpenAPI rewardsOpenAPI() {

        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Retail Rewards API")
                        .description(
                                "REST API for calculating customer rewards " +
                                "per month and total rewards for a given date range."
                        )
                        .version("1.0")
                        .license(
                                new License()
                                        .name("Apache 2.0")
                        ));
    }
}