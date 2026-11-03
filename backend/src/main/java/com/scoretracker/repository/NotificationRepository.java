package com.scoretracker.repository;

import com.scoretracker.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE n.recipient IS NULL OR n.recipient.id = :userId ORDER BY n.createdAt DESC")
    List<Notification> findRecentNotificationsForUser(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(n) FROM Notification n WHERE (n.recipient IS NULL OR n.recipient.id = :userId) AND n.readStatus = false")
    long countUnreadNotifications(@Param("userId") Long userId);
}
