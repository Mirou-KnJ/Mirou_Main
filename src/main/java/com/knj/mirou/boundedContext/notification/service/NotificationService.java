package com.knj.mirou.boundedContext.notification.service;

import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.notification.model.entity.Notification;
import com.knj.mirou.boundedContext.notification.model.enums.NotiType;
import com.knj.mirou.boundedContext.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void create(Member member, String contents, String imgUrl, NotiType notiType) {

        Notification notification = Notification.builder()
                .member(member)
                .contents(contents)
                .imgUrl(imgUrl)
                .notiType(notiType)
                .readDate(null)
                .build();

        notificationRepository.save(notification);
    }

    public List<Notification> getMyNotifications(Member member) {

        return notificationRepository.findAllByMember(member);
    }

}
