package com.library.gateway.api;

import com.library.gateway.api.dto.BorrowingDetailsResponse;
import com.library.gateway.application.LibraryCompositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/library")
@RequiredArgsConstructor
public class LibraryAggregationController {

    private final LibraryCompositionService compositionService;

    @GetMapping("/borrowings/{id}/details")
    public Mono<BorrowingDetailsResponse> getBorrowingDetails(@PathVariable(name = "id") Long id) {
        return compositionService.getBorrowingDetails(id);
    }
}