package com.library.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book")
@Getter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private boolean available = true;

    public void reserve() {
        if (!available) {
            throw new IllegalStateException("Book is already reserved");
        }
        this.available = false;
    }

    public void release() {
        if (available) {
            throw new IllegalStateException("Book is not reserved");
        }
        this.available = true;
    }
}