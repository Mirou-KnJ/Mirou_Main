package com.knj.mirou.boundedContext.point.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
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

    @OneToOne
    private Member owner;

    private int currentPoint;

    private int totalGetPoint;

    private int totalUserPoint;

}
