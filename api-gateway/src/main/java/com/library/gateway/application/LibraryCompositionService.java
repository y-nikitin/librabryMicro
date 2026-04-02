package com.library.gateway.application;

import com.library.gateway.api.dto.BookResponse;
import com.library.gateway.api.dto.BorrowingDetailsResponse;
import com.library.gateway.api.dto.BorrowingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LibraryCompositionService {

    private final WebClient webClient;

    @Value("${services.borrowing.base-url}")
    private String borrowingBaseUrl;

    @Value("${services.book.base-url}")
    private String bookBaseUrl;

    public Mono<BorrowingDetailsResponse> getBorrowingDetails(Long borrowingId) {
        Mono<BorrowingResponse> borrowingMono = webClient.get()
                .uri(borrowingBaseUrl + "/api/v1/borrowings/{id}", borrowingId)
                .retrieve()
                .bodyToMono(BorrowingResponse.class);

        return borrowingMono.flatMap(borrowing -> {
            Mono<BookResponse> bookMono = webClient.get()
                    .uri(bookBaseUrl + "/api/v1/books/{id}", borrowing.getBookId())
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