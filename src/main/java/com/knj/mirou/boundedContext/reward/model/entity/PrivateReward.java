package com.knj.mirou.boundedContext.reward.model.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.reward.model.enums.RewardType;
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
public class PrivateReward extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge linkedChallenge;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChallengeMember linkedChallengeMember;

    private int round;

    @Enumerated(EnumType.STRING)
    private RewardType rewardType;

    private String reward;

}
