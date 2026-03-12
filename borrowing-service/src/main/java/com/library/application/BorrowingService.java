package com.library.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.application.event.BorrowingCreatedPayload;
import com.library.domain.Borrowing;
import com.library.domain.OutboxEvent;
import com.library.infrastructure.persistence.BorrowingRepository;
import com.library.api.dto.CreateBorrowingRequest;
import com.library.infrastructure.client.BookServiceClient;
import com.library.infrastructure.persistence.OutboxEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookServiceClient bookClient;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Long createBorrowing(CreateBorrowingRequest request) {
        Long bookId = request.bookId();
        String borrower = request.borrower();

        // 1) reserve в book-service
        bookClient.reserveBook(bookId);

        try {
            // 2) save в своїй БД
            Borrowing saved = borrowingRepository.save(new Borrowing(bookId, borrower));

            createOutboxEvent(saved);
            return saved.getId();
        } catch (RuntimeException e) {
            // 3) компенсація: якщо не змогли зберегти borrowing — відпускаємо книгу
            try {
                bookClient.releaseBook(bookId);
            } catch (Exception ignored) {
                // логувати бажано, але не маскуємо первинну помилку
            }
            throw e;
        }
    }

    private void createOutboxEvent(Borrowing saved) {
        try {
            BorrowingCreatedPayload payload = new BorrowingCreatedPayload(
                    saved.getId(),
                    saved.getBookId(),
                    saved.getBorrower(),
                    OffsetDateTime.now()
            );

            String payloadJson = objectMapper.writeValueAsString(payload);

            OutboxEvent outboxEvent = new OutboxEvent(
                    "BORROWING",
                    saved.getId().toString(),
                    "BORROWING_CREATED",
                    payloadJson
            );

            outboxEventRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize outbox payload", e);
        }
    }
}