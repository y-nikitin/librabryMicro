package com.library.api;

import com.library.application.BorrowingService;
import com.library.api.dto.CreateBorrowingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/borrowings")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createBorrowing(@RequestBody CreateBorrowingRequest req) {
        return borrowingService.createBorrowing(req);
    }
}