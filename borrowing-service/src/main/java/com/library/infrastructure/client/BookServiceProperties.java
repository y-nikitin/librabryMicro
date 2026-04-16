package com.library.infrastructure.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "book-service")
public class BookServiceProperties {

    private String baseUrl;

    private String booksPath;
}
