package com.library.infrastructure.client;  // рекомендований пакет

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class BookServiceClient {

    private final RestClient restClient;

    @Value("${book-service.books-path}")
    private String booksPath;

    public void reserveBook(Long bookId) {
        restClient.post()
                .uri(booksPath + "/{id}/reserve", bookId)
                .retrieve()
                .toBodilessEntity();
    }

    public void releaseBook(Long bookId) {
        restClient.post()
                .uri(booksPath + "/{id}/release", bookId)
                .retrieve()
                .toBodilessEntity();
    }
}