package com.knj.mirou.boundedContext.point.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Date;

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
