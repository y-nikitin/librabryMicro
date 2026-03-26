package com.library.api.dto;

import java.time.OffsetDateTime;

public record BookReservationFailedPayload(
        Long borrowingId,
        Long bookId,
        String borrower,
        String reason,
        OffsetDateTime occurredAt
) {}
