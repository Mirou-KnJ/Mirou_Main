package com.knj.mirou.boundedContext.notification.service;

import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import com.knj.mirou.boundedContext.notification.model.entity.Notification;
import com.knj.mirou.boundedContext.notification.model.enums.NotiType;
import com.knj.mirou.boundedContext.notification.repository.NotificationRepository;
import com.knj.mirou.boundedContext.reportHistory.service.ReportHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final MemberService memberService;
    private final ImageDataService imageDataService;
    private final ReportHistoryService reportHistoryService;

    private final NotificationRepository notificationRepository;

    @Transactional
    public void create(Member member, String contents, String imgUrl, NotiType notiType) {

        String optimizedUrl = imageDataService.getOptimizingUrl(imgUrl, OptimizerOption.HISTORY);
        String processingContents = processingContentsByType(contents, notiType);

        Notification notification = Notification.builder()
                .member(member)
                .contents(processingContents)
                .imgUrl(optimizedUrl)
                .notiType(notiType)
                .readDate(null)
                .build();

        notificationRepository.save(notification);
    }

    public String processingContentsByType(String contents, NotiType notiType) {

        String processingContents = "";

        switch(notiType) {
            case JOIN -> processingContents = contents.concat("님의 회원가입을 환영합니다");
            case JOIN_CHALLENGE -> processingContents = contents.concat(" 참여를 시작하였습니다");
            case END_PROGRESS -> processingContents = contents.concat(" 참여가 종료되었습니다");
            case GET_COIN -> processingContents = contents.concat("으로 코인을 획득하였습니다");
            case RESET_POINT -> processingContents = contents.concat("님, 이번주 일상지원금이 지급되었어요!");
            case BUY_PRODUCT -> processingContents = contents.concat("을 구매하였습니다");
            case REPORT_COUNT -> processingContents = contents.concat("개의 신고를 지난주에 받았어요.");
            case LIKE_COUNT -> processingContents = contents.concat("개의 좋아요를 지난주에 받았어요.");
            default -> processingContents = contents.concat("NONE_TYPE");
        }

        return processingContents;
    }

    public List<Notification> getMyNotifications(Member member) {
        return notificationRepository.findAllByMember(member);
    }

    @Transactional
    @Scheduled(cron = "3 * * * * ?")
    public void sendWeeklyNotification() {

        //FIXME
        Member member = memberService.getById(4L).get();

        reportHistoryService.getWeeklyReportedCounts(member);
    }
}
