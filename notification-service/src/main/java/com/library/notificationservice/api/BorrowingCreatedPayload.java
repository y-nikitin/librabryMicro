package com.library.notificationservice.api;

import java.time.OffsetDateTime;

public record BorrowingCreatedPayload(
        Long borrowingId,
        Long bookId,
        String borrower,
        OffsetDateTime occurredAt
) {}
