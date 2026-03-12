package com.library.infrastructure.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String BORROWING_EXCHANGE = "borrowing.exchange";
    public static final String BORROWING_CREATED_QUEUE = "borrowing.created.queue";
    public static final String BORROWING_CREATED_ROUTING_KEY = "borrowing.created";

    @Bean
    public TopicExchange borrowingExchange() {
        return new TopicExchange(BORROWING_EXCHANGE);
    }

    @Bean
    public Queue borrowingCreatedQueue() {
        return new Queue(BORROWING_CREATED_QUEUE, true);
    }

    @Bean
    public Binding borrowingCreatedBinding() {
        return BindingBuilder
                .bind(borrowingCreatedQueue())
                .to(borrowingExchange())
                .with(BORROWING_CREATED_ROUTING_KEY);
    }
}