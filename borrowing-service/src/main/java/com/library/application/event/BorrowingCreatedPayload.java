package com.library.application.event;

import java.time.OffsetDateTime;

public record BorrowingCreatedPayload(
        Long borrowingId,
        Long bookId,
        String borrower,
        OffsetDateTime occurredAt
) {}