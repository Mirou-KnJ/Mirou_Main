package com.knj.mirou.boundedContext.notification.repository;

import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.notification.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByMember(Member member);

    List<Notification> findTop20ByMemberOrderByCreateDateDesc(Member member);
}
