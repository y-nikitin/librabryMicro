package com.library.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "borrowings")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Borrowing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private String borrower;

    @Column(name = "borrowed_at", nullable = false)
    private OffsetDateTime borrowedAt;

    public Borrowing(Long bookId, String borrower) {
        this.bookId = bookId;
        this.borrower = borrower;
        this.borrowedAt = OffsetDateTime.now();
    }
}