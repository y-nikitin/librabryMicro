package com.library.application;

import com.library.domain.Book;
import com.library.infrastructure.persistence.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    //додаючи Transactional, ми переконуємось що зміни гарантовано фіксуються в базі при успішному завершенні методу
    @Transactional
    public void reserveBook(Long bookId) {
        log.info("reserving book with id {}", bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with the given id" + bookId));

        book.reserve();
    }

    @Transactional
    public void releaseBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found: " + bookId));

        book.release();
    }
}
