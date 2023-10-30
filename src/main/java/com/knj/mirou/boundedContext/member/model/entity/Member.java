package com.knj.mirou.boundedContext.member.model.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.coin.entity.Coin;
import com.knj.mirou.boundedContext.member.model.enums.MemberRole;
import com.knj.mirou.boundedContext.point.entity.Point;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Member extends BaseEntity {

    private String loginId;

    private String nickname;

    private String socialCode;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String inviteCode;

    @OneToOne(mappedBy = "owner")
    private Coin coin;

    @OneToOne(mappedBy = "owner")
    private Point point;

}
