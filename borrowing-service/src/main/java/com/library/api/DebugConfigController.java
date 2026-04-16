package com.library.api;

import com.library.infrastructure.client.BookServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/debug/config")
@RequiredArgsConstructor
public class DebugConfigController {

    private final BookServiceProperties bookServiceProperties;

    @GetMapping
    public Map<String, String> currentConfig() {
        return Map.of(
                "book-service.base-url", bookServiceProperties.getBaseUrl(),
                "book-service.books-path", bookServiceProperties.getBooksPath()
        );
    }
}
