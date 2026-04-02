package com.library.gateway.api.dto;

import lombok.Data;

@Data
public class BorrowingResponse {
    private Long id;
    private String borrower;
    private Long bookId;
    private String status;
}
