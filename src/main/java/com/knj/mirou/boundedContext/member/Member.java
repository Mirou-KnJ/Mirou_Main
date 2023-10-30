package com.knj.mirou.boundedContext.member;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.coin.entity.Coin;
import com.knj.mirou.boundedContext.point.Point;
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
public class Member extends BaseEntity {

    private String loginId;

    private String nickname;

    private String socialCode;

    private MemberRole role;

    private String inviteCode;

    @OneToOne(mappedBy = "owner")
    private Coin coin;

}
