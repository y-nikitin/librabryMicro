package com.library.api;

import com.library.api.dto.BookSummaryResponse;
import com.library.application.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/{id}")
    public BookSummaryResponse getBook(@RequestHeader("X-User-Id") String userId,
                                       @RequestHeader("X-Roles") String roles,
                                       @PathVariable("id") Long id) {
        return bookService.getBookSummary(id);
    }

    @PostMapping("/{id}/reserve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reserveBook(@PathVariable("id") Long id) {
        bookService.reserveBook(id);
    }

    @PostMapping("/{id}/release")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void releaseBook(@PathVariable("id") Long id) {
        bookService.releaseBook(id);
    }
}
