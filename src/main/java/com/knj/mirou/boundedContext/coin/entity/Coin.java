package com.knj.mirou.boundedContext.coin.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.member.Member;
import jakarta.persistence.*;
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
public class Coin extends BaseEntity {

    @OneToOne
    private Member owner;

    private int currentCoin;

    private int totalGetCoin;

    private int totalUserCoin;
}
