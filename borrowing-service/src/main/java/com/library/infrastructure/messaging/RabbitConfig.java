package com.library.infrastructure.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String LIBRARY_EXCHANGE = "library.exchange";

    public static final String BOOK_RESERVED_QUEUE = "borrowing.book-reserved.queue";
    public static final String BOOK_RESERVATION_FAILED_QUEUE = "borrowing.book-reservation-failed.queue";

    public static final String BOOK_RESERVED_ROUTING_KEY = "book.reserved";
    public static final String BOOK_RESERVATION_FAILED_ROUTING_KEY = "book.reservation-failed";

    public static final String BORROWING_CREATED_ROUTING_KEY = "borrowing.created";
    public static final String BORROWING_APPROVED_ROUTING_KEY = "borrowing.approved";
    public static final String BORROWING_CANCELLED_ROUTING_KEY = "borrowing.cancelled";

    @Bean
    public TopicExchange libraryExchange() {
        return new TopicExchange(LIBRARY_EXCHANGE);
    }

    @Bean
    public Queue bookReservedQueue() {
        return QueueBuilder.durable(BOOK_RESERVED_QUEUE).build();
    }

    @Bean
    public Queue bookReservationFailedQueue() {
        return QueueBuilder.durable(BOOK_RESERVATION_FAILED_QUEUE).build();
    }

    @Bean
    public Binding bookReservedBinding() {
        return BindingBuilder.bind(bookReservedQueue())
                .to(libraryExchange())
                .with(BOOK_RESERVED_ROUTING_KEY);
    }

    @Bean
    public Binding bookReservationFailedBinding() {
        return BindingBuilder.bind(bookReservationFailedQueue())
                .to(libraryExchange())
                .with(BOOK_RESERVATION_FAILED_ROUTING_KEY);
    }
}