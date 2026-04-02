package com.library.notificationservice.infrastructure.persistance;

import com.library.notificationservice.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findTopByBorrowingIdOrderByIdDesc(Long borrowingId);
}
