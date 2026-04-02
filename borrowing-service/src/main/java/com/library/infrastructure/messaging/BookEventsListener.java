package com.library.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.api.dto.BookReservationFailedPayload;
import com.library.api.dto.BookReservedPayload;
import com.library.application.BorrowingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookEventsListener {

    private final BorrowingService borrowingService;
    private final ObjectMapper objectMapper;

//    @RabbitListener(queues = RabbitConfig.BOOK_RESERVED_QUEUE)
    public void handleBookReserved(String message) throws Exception {
        BookReservedPayload payload = objectMapper.readValue(message, BookReservedPayload.class);

        log.info("Received BOOK_RESERVED for borrowingId={}, bookId={}",
                payload.borrowingId(), payload.bookId());

        borrowingService.approveBorrowing(payload.borrowingId());
    }

//    @RabbitListener(queues = RabbitConfig.BOOK_RESERVATION_FAILED_QUEUE)
    public void handleBookReservationFailed(String message) throws Exception {
        BookReservationFailedPayload payload = objectMapper.readValue(message, BookReservationFailedPayload.class);

        log.info("Received BOOK_RESERVATION_FAILED for borrowingId={}, bookId={}, reason={}",
                payload.borrowingId(), payload.bookId(), payload.reason());

        borrowingService.cancelBorrowing(payload.borrowingId(), payload.reason());
    }
}
