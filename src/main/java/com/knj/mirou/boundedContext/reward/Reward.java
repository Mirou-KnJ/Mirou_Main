package com.knj.mirou.boundedContext.reward;

import com.knj.mirou.boundedContext.challenge.entity.Challenge;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Reward {
    @Id
    private long id;

    @ManyToOne
    private Challenge linkedChallenge;

    private int round; //라운드
    private int rewardCoin; //보상 코인
}
