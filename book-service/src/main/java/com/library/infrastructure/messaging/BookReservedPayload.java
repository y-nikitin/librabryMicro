package com.library.infrastructure.messaging;

import java.time.OffsetDateTime;

public record BookReservedPayload(
        Long borrowingId,
        Long bookId,
        String borrower,
        OffsetDateTime occurredAt
) {}
