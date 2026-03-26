package com.library.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String LIBRARY_EXCHANGE = "library.exchange";

    public static final String BORROWING_CREATED_QUEUE = "book.borrowing-created.queue";
    public static final String BOOK_RESERVED_QUEUE = "borrowing.book-reserved.queue";
    public static final String BOOK_RESERVATION_FAILED_QUEUE = "borrowing.book-reservation-failed.queue";

    public static final String BORROWING_CREATED_ROUTING_KEY = "borrowing.created";
    public static final String BOOK_RESERVED_ROUTING_KEY = "book.reserved";
    public static final String BOOK_RESERVATION_FAILED_ROUTING_KEY = "book.reservation-failed";

    @Bean
    public TopicExchange libraryExchange() {
        return new TopicExchange(LIBRARY_EXCHANGE);
    }

    @Bean
    public Queue borrowingCreatedQueue() {
        return QueueBuilder.durable(BORROWING_CREATED_QUEUE).build();
    }

    @Bean
    public Binding borrowingCreatedBinding() {
        return BindingBuilder
                .bind(borrowingCreatedQueue())
                .to(libraryExchange())
                .with(BORROWING_CREATED_ROUTING_KEY);
    }
}
