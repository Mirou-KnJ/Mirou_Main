package com.knj.mirou.boundedContext.coin.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.member.entity.Member;
import jakarta.persistence.*;
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
public class Coin extends BaseEntity {

    @OneToOne
    private Member owner;

    private int currentCoin;

    private int totalGetCoin;

    private int totalUserCoin;
}
