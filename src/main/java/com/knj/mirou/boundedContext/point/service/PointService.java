package com.knj.mirou.boundedContext.point.service;

import com.knj.mirou.base.enums.ChangeType;
import com.knj.mirou.base.event.EventAfterResetPoint;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.point.entity.Point;
import com.knj.mirou.boundedContext.point.repository.PointRepository;
import com.knj.mirou.boundedContext.pointhistory.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {

    private final PointHistoryService pointHistoryService;

    private final ApplicationEventPublisher publisher;

    private final PointRepository pointRepository;

    private static final String SYSTEM_IMG = "https://kr.object.ncloudstorage.com/mirou/etc/system_noti.png";

    @Transactional
    public Point create() {

        Point createPoint = Point.builder()
                .currentPoint(3000)
                .totalGetPoint(3000)
                .totalUsedPoint(0)
                .build();

        return pointRepository.save(createPoint);
    }

    @Transactional
    public RsData<Long> usingJoinPoint(Member member, Challenge challenge) {

        Point point = member.getPoint();
        int joinCost = challenge.getJoinCost();

        if (joinCost > point.getCurrentPoint()) {
            return RsData.of("F-1", "참가 비용이 부족합니다.", point.getId());
        }

        usingPoint(point, joinCost);
        pointHistoryService.create(member, ChangeType.USED, joinCost,
                challenge.getName() + " 사용", challenge.getImgUrl());

        return RsData.of("S-1", "포인트 사용이 완료되었습니다.", point.getId());
    }

    @Transactional
    public void usingPoint(Point point, int cost) {

        point = Point.builder()
                .id(point.getId())
                .currentPoint(point.getCurrentPoint() - cost)
                .totalUsedPoint(point.getTotalUsedPoint() + cost)
                .totalGetPoint(point.getTotalGetPoint())
                .build();

        pointRepository.save(point);
    }

    @Transactional
    public void resetPoint(List<Member> targetMembers, int resetStandard) {

        for (Member targetMember : targetMembers) {
            Point point = targetMember.getPoint();

            int gavePoint = resetStandard - point.getCurrentPoint();

            point = Point.builder()
                    .id(point.getId())
                    .currentPoint(resetStandard)
                    .totalGetPoint(point.getTotalGetPoint() + gavePoint)
                    .totalUsedPoint(point.getTotalUsedPoint())
                    .build();

            pointRepository.save(point);

            pointHistoryService.create(targetMember, ChangeType.GET, gavePoint,
                    "이번 주 일상지원금 지급", SYSTEM_IMG);

            log.info("지급액 : " + gavePoint);

            publisher.publishEvent(new EventAfterResetPoint(this, targetMember));
        }
    }
}
