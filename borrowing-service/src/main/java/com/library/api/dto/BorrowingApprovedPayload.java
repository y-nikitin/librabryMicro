package com.library.api.dto;

import java.time.OffsetDateTime;

public record BorrowingApprovedPayload(
        Long borrowingId,
        Long bookId,
        String borrower,
        OffsetDateTime occurredAt
) {}
