package com.knj.mirou.boundedContext.pointhistory.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
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
public class PointHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member linkedMember;

    private int changePoint;

    private String PointContents;

}
