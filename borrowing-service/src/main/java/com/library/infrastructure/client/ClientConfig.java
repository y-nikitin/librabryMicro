package com.library.infrastructure.client;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {

    private final BookServiceProperties bookServiceProperties;

    @Bean
    @RefreshScope
    RestClient restClient(RestClient.Builder builder) {
        var httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(500))
                .build();

        var requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofMillis(800));

        return builder
                .baseUrl(bookServiceProperties.getBaseUrl())
                .requestFactory(requestFactory)
                .build();
    }
}