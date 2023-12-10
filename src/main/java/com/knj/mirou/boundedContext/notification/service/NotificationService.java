package com.knj.mirou.boundedContext.notification.service;

import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
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

    private final ImageDataService imageDataService;

    private final NotificationRepository notificationRepository;

    @Transactional
    public void create(Member member, String contents, String imgUrl, NotiType notiType) {

        String optimizedUrl = imageDataService.getOptimizingUrl(imgUrl, OptimizerOption.HISTORY);

        Notification notification = Notification.builder()
                .member(member)
                .contents(contents)
                .imgUrl(optimizedUrl)
                .notiType(notiType)
                .readDate(null)
                .build();

        notificationRepository.save(notification);
    }

    public List<Notification> getMyNotifications(Member member) {
        return notificationRepository.findAllByMember(member);
    }
}
