package com.library.infrastructure.client;  // рекомендований пакет

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookServiceClient {

    private final RestClient restClient;

    @Value("${book-service.books-path}")
    private String booksPath;

    @Retry(name = "bookService", fallbackMethod = "reserveFallback")
    @CircuitBreaker(name = "bookService", fallbackMethod = "reserveFallback")
    public void reserveBook(Long bookId) {
        String correlation = MDC.get(CorrelationIdFilter.MDC_KEY);

        log.info("reserving book with id {}", bookId);
        log.info("corelation header {}", correlation);

        restClient.post()
                .uri(booksPath + "/{id}/reserve", bookId)
                .header(CorrelationIdFilter.HEADER, correlation)
                .retrieve()
                .toBodilessEntity();
    }

    public void releaseBook(Long bookId) {
        restClient.post()
                .uri(booksPath + "/{id}/release", bookId)
                .retrieve()
                .toBodilessEntity();
    }

    private void reserveFallback(Long bookId, Throwable throwable) {
        log.warn("reserveFallback triggered for bookId={}, ex={}", bookId, throwable.toString());
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "book-service is unavailable (reserve failed)",
                throwable);
    }
}