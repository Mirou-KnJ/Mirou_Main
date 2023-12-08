package com.knj.mirou.boundedContext.notification.repository;

import com.knj.mirou.boundedContext.notification.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
