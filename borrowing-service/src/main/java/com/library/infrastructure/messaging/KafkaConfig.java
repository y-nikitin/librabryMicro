package com.library.infrastructure.messaging;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    public static final String BORROWING_CREATED_TOPIC = "borrowing.created";
}
