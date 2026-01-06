package com.spring.slik_v2_server.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${spring.Dodam.API_KEY}")
    private String apiKey;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(apiKey)
                .build();
    }
}
