package com.library.api.dto;

public record CreateBorrowingRequest(
        Long bookId,
        String borrower
) {}