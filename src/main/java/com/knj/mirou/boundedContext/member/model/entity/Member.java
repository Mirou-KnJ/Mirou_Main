package com.knj.mirou.boundedContext.member.model.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.coin.entity.Coin;
import com.knj.mirou.boundedContext.inventory.model.entity.Inventory;
import com.knj.mirou.boundedContext.member.model.enums.MemberRole;
import com.knj.mirou.boundedContext.member.model.enums.SocialCode;
import com.knj.mirou.boundedContext.point.entity.Point;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class Member extends BaseEntity {

    @Column(unique = true)
    private String loginId;

    @Column(unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private SocialCode socialCode;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String inviteCode;

    private String imgUrl;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(referencedColumnName = "id")
    private Coin coin;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(referencedColumnName = "id")
    private Point point;

    @OneToMany(mappedBy = "owner")
    private List<Inventory> inventory;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
