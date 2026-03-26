package com.library.infrastructure.messaging;

import java.time.OffsetDateTime;

public record BorrowingCreatedPayload(
        Long borrowingId,
        Long bookId,
        String borrower,
        OffsetDateTime occurredAt
) {}
