package com.knj.mirou.boundedContext.point.service;

import com.knj.mirou.boundedContext.point.entity.Point;
import com.knj.mirou.boundedContext.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
