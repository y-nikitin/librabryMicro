package com.library.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.application.BookSagaService;
import com.library.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BorrowingCreatedListener {

    private final BookSagaService bookSagaService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitConfig.BORROWING_CREATED_QUEUE)
    public void handle(String message) throws JsonProcessingException {
        String json = objectMapper.readValue(message, String.class);
        BorrowingCreatedPayload payload = objectMapper.readValue(json, BorrowingCreatedPayload.class);
        log.info("Received BORROWING_CREATED for borrowingId={}, bookId={}",
                payload.borrowingId(), payload.bookId());

        bookSagaService.handleBorrowingCreated(payload);
    }
}
