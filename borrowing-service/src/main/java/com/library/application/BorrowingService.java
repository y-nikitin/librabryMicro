package com.library.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.api.dto.BorrowingApprovedPayload;
import com.library.api.dto.BorrowingCancelledPayload;
import com.library.api.dto.BorrowingSummaryResponse;
import com.library.application.event.BorrowingCreatedPayload;
import com.library.domain.Borrowing;
import com.library.domain.OutboxEvent;
import com.library.infrastructure.persistence.BorrowingRepository;
import com.library.api.dto.CreateBorrowingRequest;
import com.library.infrastructure.persistence.OutboxEventRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public BorrowingSummaryResponse getBorrowingSummary(Long id) {
        Borrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Borrowing not found: " + id));

        return BorrowingSummaryResponse.builder()
                .id(borrowing.getId())
                .borrower(borrowing.getBorrower())
                .bookId(borrowing.getBookId())
                .status(borrowing.getStatus() != null ? borrowing.getStatus().name() : null)
                .build();
    }

    @Transactional
    public Long createBorrowing(CreateBorrowingRequest request) {
        Borrowing saved = borrowingRepository.save(
                new Borrowing(
                        request.bookId(),
                        request.borrower()
                )
        );

        createOutboxEvent(saved);
        return saved.getId();
    }

    @Transactional
    public void approveBorrowing(Long borrowingId) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new EntityNotFoundException("Borrowing not found: " + borrowingId));

        borrowing.approve();
        createBorrowingApprovedOutboxEvent(borrowing);
    }

    @Transactional
    public void cancelBorrowing(Long borrowingId, String reason) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new EntityNotFoundException("Borrowing not found: " + borrowingId));

        borrowing.cancel();
        createBorrowingCancelledOutboxEvent(borrowing, reason);
    }

    private void createBorrowingApprovedOutboxEvent(Borrowing borrowing) {
        BorrowingApprovedPayload payload = new BorrowingApprovedPayload(
                borrowing.getId(),
                borrowing.getBookId(),
                borrowing.getBorrower(),
                OffsetDateTime.now()
        );

        saveOutboxEvent(
                "BORROWING",
                borrowing.getId().toString(),
                "BORROWING_APPROVED",
                payload
        );
    }


    private void createBorrowingCreatedOutboxEvent(Borrowing saved) {
        BorrowingCreatedPayload payload = new BorrowingCreatedPayload(
                saved.getId(),
                saved.getBookId(),
                saved.getBorrower(),
                OffsetDateTime.now()
        );

        saveOutboxEvent(
                "BORROWING",
                saved.getId().toString(),
                "BORROWING_CREATED",
                payload
        );
    }

    private void createBorrowingCancelledOutboxEvent(Borrowing borrowing, String reason) {
        BorrowingCancelledPayload payload = new BorrowingCancelledPayload(
                borrowing.getId(),
                borrowing.getBookId(),
                borrowing.getBorrower(),
                reason,
                OffsetDateTime.now()
        );

        saveOutboxEvent(
                "BORROWING",
                borrowing.getId().toString(),
                "BORROWING_CANCELLED",
                payload
        );
    }

    private void saveOutboxEvent(String aggregateType,
                                 String aggregateId,
                                 String eventType,
                                 Object payload) {
        try {
            String payloadJson = objectMapper.writeValueAsString(payload);

            OutboxEvent outboxEvent = new OutboxEvent(
                    aggregateType,
                    aggregateId,
                    eventType,
                    payloadJson
            );

            outboxEventRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize outbox payload", e);
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