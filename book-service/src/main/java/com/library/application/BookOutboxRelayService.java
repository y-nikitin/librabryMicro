package com.library.application;
import com.library.config.RabbitConfig;
import com.library.domain.OutboxEvent;
import com.library.infrastructure.persistence.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookOutboxRelayService {

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void relay() {
        List<OutboxEvent> events = new ArrayList<>();
        events.addAll(outboxEventRepository.findTop20ByStatusOrderByCreatedAtAsc("NEW"));
        events.addAll(outboxEventRepository.findTop20ByStatusOrderByCreatedAtAsc("FAILED"));

        for (OutboxEvent event : events) {
            try {
                String routingKey = resolveRoutingKey(event.getEventType());

                rabbitTemplate.convertAndSend(
                        RabbitConfig.LIBRARY_EXCHANGE,
                        routingKey,
                        event.getPayload()
                );

                event.markSent();
                log.info("Outbox event {} sent successfully", event.getEventId());
            } catch (Exception e) {
                event.markFailed();
                log.error("Failed to send outbox event {}: {}", event.getEventId(), e.getMessage(), e);
            }
        }
    }

    private String resolveRoutingKey(String eventType) {
        return switch (eventType) {
            case "BOOK_RESERVED" -> RabbitConfig.BOOK_RESERVED_ROUTING_KEY;
            case "BOOK_RESERVATION_FAILED" -> RabbitConfig.BOOK_RESERVATION_FAILED_ROUTING_KEY;
            default -> throw new IllegalArgumentException("Unsupported event type: " + eventType);
        };
    }
}
