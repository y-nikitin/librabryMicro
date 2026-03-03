package com.library.infrastructure.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfig {

    @Value("${book-service.base-url}")
    private String bookBaseUrl;

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder.baseUrl(bookBaseUrl).build();
    }
}