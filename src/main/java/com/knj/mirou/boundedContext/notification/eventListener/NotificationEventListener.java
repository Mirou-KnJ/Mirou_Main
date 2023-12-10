package com.knj.mirou.boundedContext.notification.eventListener;

import com.knj.mirou.base.event.*;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.notification.model.enums.NotiType;
import com.knj.mirou.boundedContext.notification.service.NotificationService;
import com.knj.mirou.boundedContext.product.model.entity.ProductInfo;
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
    private static final String SYSTEM_IMG = "https://kr.object.ncloudstorage.com/mirou/etc/speaker.png";

    @EventListener
    @Transactional
    public void listen(EventAfterGiveCoin event) {

        Member member = event.getMember();
        String contents = event.getContents();
        String imgUrl = event.getImgUrl();

        notificationService.create(member, contents, imgUrl, NotiType.GET_COIN);
    }

    @EventListener
    @Transactional
    public void listen(EventAfterJoinChallenge event) {

        ChallengeMember challengeMember = event.getChallengeMember();
        Challenge challenge = challengeMember.getLinkedChallenge();
        Member member = challengeMember.getLinkedMember();

        notificationService.create(member, challenge.getName(), challenge.getImgUrl(), NotiType.JOIN_CHALLENGE);
    }

    @EventListener
    @Transactional
    public void listen(EventAfterEndProgress event) {

        ChallengeMember challengeMember = event.getChallengeMember();
        Challenge challenge = challengeMember.getLinkedChallenge();
        Member member = challengeMember.getLinkedMember();

        notificationService.create(member, challenge.getName(), challenge.getImgUrl(), NotiType.END_PROGRESS);
    }

    @EventListener
    @Transactional
    public void listen(EventAfterResetPoint event) {

        Member member = event.getMember();

        notificationService.create(member, member.getNickname(), SYSTEM_IMG, NotiType.RESET_POINT);
    }

    @EventListener
    @Transactional
    public void listen(EventAfterBuyProduct event) {

        Member member = event.getMember();
        ProductInfo productInfo = event.getProductInfo();

        notificationService.create(member, productInfo.getName(), productInfo.getImgUrl(), NotiType.BUY_PRODUCT);
    }
}
