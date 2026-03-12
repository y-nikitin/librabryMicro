package com.library.notificationservice.application;

import com.library.notificationservice.api.BorrowingCreatedPayload;
import com.library.notificationservice.domain.Notification;
import com.library.notificationservice.infrastructure.persistance.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public void sendNotification(BorrowingCreatedPayload payload) {

        String message = "User %s borrowed book %d"
                .formatted(payload.borrower(), payload.bookId());

        Notification notification =
                new Notification(payload.borrowingId(), message);

        repository.save(notification);
    }
}
