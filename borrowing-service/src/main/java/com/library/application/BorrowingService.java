package com.library.application;

import com.library.domain.Borrowing;
import com.library.infrastructure.persistence.BorrowingRepository;
import com.library.api.dto.CreateBorrowingRequest;
import com.library.infrastructure.client.BookServiceClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookServiceClient bookClient;

    @Transactional
    public Long createBorrowing(CreateBorrowingRequest request) {
        Long bookId = request.bookId();
        String borrower = request.borrower();

        // 1) reserve в book-service
        bookClient.reserveBook(bookId);

        try {
            // 2) save в своїй БД
            Borrowing saved = borrowingRepository.save(new Borrowing(bookId, borrower));
            return saved.getId();
        } catch (RuntimeException e) {
            // 3) компенсація: якщо не змогли зберегти borrowing — відпускаємо книгу
            // (потрібен endpoint release у book-service)
            try {
                bookClient.releaseBook(bookId);
            } catch (Exception ignored) {
                // логувати бажано, але не маскуємо первинну помилку
            }
            throw e;
        }
    }
}