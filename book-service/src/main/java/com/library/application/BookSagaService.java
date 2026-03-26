package com.library.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.domain.OutboxEvent;
import com.library.infrastructure.messaging.BookReservationFailedPayload;
import com.library.infrastructure.messaging.BookReservedPayload;
import com.library.infrastructure.messaging.BorrowingCreatedPayload;
import com.library.infrastructure.persistence.OutboxEventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookSagaService {

    private final BookService bookService;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void handleBorrowingCreated(BorrowingCreatedPayload payload) {
        try {
            bookService.reserveBook(payload.bookId());

            BookReservedPayload eventPayload = new BookReservedPayload(
                    payload.borrowingId(),
                    payload.bookId(),
                    payload.borrower(),
                    OffsetDateTime.now()
            );

            saveOutboxEvent(
                    "BOOK",
                    payload.bookId().toString(),
                    "BOOK_RESERVED",
                    eventPayload
            );

            log.info("Book reserved successfully for borrowingId={}, bookId={}",
                    payload.borrowingId(), payload.bookId());

        } catch (EntityNotFoundException | IllegalStateException e) {
            BookReservationFailedPayload eventPayload = new BookReservationFailedPayload(
                    payload.borrowingId(),
                    payload.bookId(),
                    payload.borrower(),
                    e.getMessage(),
                    OffsetDateTime.now()
            );

            saveOutboxEvent(
                    "BOOK",
                    payload.bookId().toString(),
                    "BOOK_RESERVATION_FAILED",
                    eventPayload
            );

            log.warn("Book reservation failed for borrowingId={}, bookId={}, reason={}",
                    payload.borrowingId(), payload.bookId(), e.getMessage());
        }
    }

    private void saveOutboxEvent(String aggregateType,
                                 String aggregateId,
                                 String eventType,
                                 Object payload) {
        try {
            String payloadJson = objectMapper.writeValueAsString(payload);

            OutboxEvent event = new OutboxEvent(
                    aggregateType,
                    aggregateId,
                    eventType,
                    payloadJson
            );

            outboxEventRepository.save(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize outbox payload", e);
        }
    }
}
