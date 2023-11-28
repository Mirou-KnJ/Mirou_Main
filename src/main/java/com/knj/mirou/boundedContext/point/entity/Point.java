package com.knj.mirou.boundedContext.point.entity;

import com.knj.mirou.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class Point extends BaseEntity {

    private int currentPoint;

    private int totalGetPoint;

    private int totalUsedPoint;

    public void resetCurrentPoint() {
        this.currentPoint = 3000;
    }

    public void usingPoint(int cost) {
        this.currentPoint = this.currentPoint - cost;
        this.totalUsedPoint = this.totalUsedPoint + cost;
    }
}
