package com.knj.mirou.boundedContext.point.repository;

import com.knj.mirou.boundedContext.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}
