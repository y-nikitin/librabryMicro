package com.library.notificationservice.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.notificationservice.api.BorrowingCreatedPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaNotificationConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "borrowing.created", groupId = "notification-group")
    public void listen(String message) {
        try {
            BorrowingCreatedPayload event =
                    objectMapper.readValue(message, BorrowingCreatedPayload.class);

            log.info("Received Kafka message");
            log.info("Borrowing ID: {}", event.borrowingId());
            log.info("Borrower: {}", event.borrower());
            log.info("Book ID: {}", event.bookId());
            log.info("Created At: {}", event.occurredAt());

        } catch (Exception e) {
            log.error("Failed to parse Kafka message: {}", message, e);
        }
    }
}