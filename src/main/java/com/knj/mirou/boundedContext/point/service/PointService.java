package com.knj.mirou.boundedContext.point.service;

import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import com.knj.mirou.boundedContext.point.entity.Point;
import com.knj.mirou.boundedContext.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {

    private final PointRepository pointRepository;

    @Transactional
    public Point createPoint() {

        Point createPoint = Point.builder()
                .currentPoint(3000)
                .totalGetPoint(3000)
                .totalUsedPoint(0)
                .build();

        return pointRepository.save(createPoint);
    }

    @Transactional
    public void resetPoint(List<Member> members) {

        for(Member targetMember : members) {
            Point point = targetMember.getPoint();

            if(point.getCurrentPoint() != 3000) {
                log.info("현재 포인트 : " + point.getCurrentPoint());
                point.resetCurrentPoint();
                log.info("초기화 후 포인트 : " + point.getCurrentPoint());
            }

            pointRepository.save(point);
        }
    }
}
