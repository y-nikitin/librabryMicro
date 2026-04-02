package com.library.notificationservice.api;

import com.library.notificationservice.api.dto.NotificationSummaryResponse;
import com.library.notificationservice.application.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/borrowings/{borrowingId}")
    public NotificationSummaryResponse getByBorrowingId(@PathVariable("borrowingId") Long borrowingId) {
        return notificationService.getNotificationSummary(borrowingId);
    }
}
