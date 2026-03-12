package com.library.notificationservice.infrastructure.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String BORROWING_CREATED_QUEUE = "borrowing.created.queue";

    @Bean
    public Queue borrowingCreatedQueue() {
        return new Queue(BORROWING_CREATED_QUEUE, true);
    }
}
