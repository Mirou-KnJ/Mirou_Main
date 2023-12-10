package com.knj.mirou.boundedContext.notification.eventListener;

import com.knj.mirou.base.event.EventAfterGiveCoin;
import com.knj.mirou.base.event.EventAfterWriteFeed;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.notification.model.enums.NotiType;
import com.knj.mirou.boundedContext.notification.service.NotificationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;

    @EventListener
    @Transactional
    public void listen(EventAfterGiveCoin event) {

        Member member = event.getMember();
        String contents = event.getContents() + "으로 인해";
        String imgUrl = event.getImgUrl();

        log.info("알림을 생성할게요 !! :  " + contents);

         notificationService.create(member, contents, imgUrl, NotiType.GET_COIN);
    }

}
