package com.library.notificationservice.application;

import com.library.notificationservice.api.BorrowingCreatedPayload;
import com.library.notificationservice.api.dto.NotificationSummaryResponse;
import com.library.notificationservice.domain.Notification;
import com.library.notificationservice.infrastructure.persistance.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    @Transactional
    public NotificationSummaryResponse getNotificationSummary(Long borrowingId) {
        return repository.findTopByBorrowingIdOrderByIdDesc(borrowingId)
                .map(notification -> NotificationSummaryResponse.builder()
                        .borrowingId(borrowingId)
                        .exists(true)
                        .status("Approved")
                        .build())
                .orElse(NotificationSummaryResponse.builder()
                        .borrowingId(borrowingId)
                        .exists(false)
                        .status("NOT_FOUND")
                        .build());
    }

    public void sendNotification(BorrowingCreatedPayload payload) {

        String message = "User %s borrowed book %d"
                .formatted(payload.borrower(), payload.bookId());

        Notification notification =
                new Notification(payload.borrowingId(), message);

        repository.save(notification);
    }
}
