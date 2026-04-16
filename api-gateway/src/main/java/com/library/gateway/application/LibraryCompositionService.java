package com.library.gateway.application;

import com.library.gateway.api.dto.BookResponse;
import com.library.gateway.api.dto.BorrowingDetailsResponse;
import com.library.gateway.api.dto.BorrowingResponse;
import com.library.gateway.config.ServicesProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LibraryCompositionService {

    private final WebClient webClient;
    private final ServicesProperties servicesProperties;

    public Mono<BorrowingDetailsResponse> getBorrowingDetails(Long borrowingId) {
        Mono<BorrowingResponse> borrowingMono = webClient.get()
                .uri(servicesProperties.getBorrowing().getBaseUrl() + "/api/v1/borrowings/{id}", borrowingId)
                .retrieve()
                .bodyToMono(BorrowingResponse.class);

        return borrowingMono.flatMap(borrowing -> {
            Mono<BookResponse> bookMono = webClient.get()
                    .uri(servicesProperties.getBook().getBaseUrl() + "/api/v1/books/{id}", borrowing.getBookId())
                    .retrieve()
                    .bodyToMono(BookResponse.class);

            return bookMono.map(book -> BorrowingDetailsResponse.builder()
                    .borrowingId(borrowing.getId())
                    .borrower(borrowing.getBorrower())
                    .status(borrowing.getStatus())
                    .book(book)
                    .build());
        });
    }
}