package com.library.notificationservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long borrowingId;

    private String message;

    private OffsetDateTime createdAt;

    public Notification(Long borrowingId, String message) {
        this.borrowingId = borrowingId;
        this.message = message;
        this.createdAt = OffsetDateTime.now();
    }
}
