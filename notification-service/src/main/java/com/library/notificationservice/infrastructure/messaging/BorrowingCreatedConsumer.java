package com.library.notificationservice.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.notificationservice.api.BorrowingCreatedPayload;
import com.library.notificationservice.application.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BorrowingCreatedConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitConfig.BORROWING_CREATED_QUEUE)
    public void consume(String message) {

        try {

            BorrowingCreatedPayload payload =
                    objectMapper.readValue(
                            message,
                            BorrowingCreatedPayload.class
                    );

            log.info("Received BORROWING_CREATED event {}", payload);

            notificationService.sendNotification(payload);

        } catch (Exception e) {

            log.error("Failed to process message {}", message, e);
            throw new RuntimeException(e);
        }
    }
}
