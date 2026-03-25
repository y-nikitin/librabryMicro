package com.library.application;

import com.library.domain.OutboxEvent;
import com.library.infrastructure.messaging.KafkaConfig;
import com.library.infrastructure.messaging.RabbitConfig;
import com.library.infrastructure.persistence.OutboxEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxRelayService {

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void relay() {
        log.info("Sending outbox events");
        List<OutboxEvent> events = outboxEventRepository.findTop20ByStatusOrderByCreatedAtAsc("NEW");
        events.addAll(outboxEventRepository.findTop20ByStatusOrderByCreatedAtAsc("FAILED"));

        for (OutboxEvent event : events) {
            log.info("Obtained outbox event {}", event.getEventId());
            try {
                rabbitTemplate.convertAndSend(
                        RabbitConfig.BORROWING_EXCHANGE,
                        RabbitConfig.BORROWING_CREATED_ROUTING_KEY,
                        event.getPayload()
                );

                kafkaTemplate.send(
                        KafkaConfig.BORROWING_CREATED_TOPIC,
                        event.getEventId(),
                        event.getPayload()
                ).get();

                event.markSent();
                log.info("Outbox event {} sent successfully", event.getEventId());

            } catch (Exception e) {
                log.error("Failed to send outbox event {}: {}", event.getEventId(), e.getMessage(), e);
                event.markFailed();
            }
        }
    }
}
