package com.library.gateway.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BorrowingDetailsResponse {
    private Long borrowingId;
    private String borrower;
    private String status;
    private BookResponse book;
}
