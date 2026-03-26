package com.library.api.dto;

import java.time.OffsetDateTime;

public record BorrowingCancelledPayload(
        Long borrowingId,
        Long bookId,
        String borrower,
        String reason,
        OffsetDateTime occurredAt
) {}
