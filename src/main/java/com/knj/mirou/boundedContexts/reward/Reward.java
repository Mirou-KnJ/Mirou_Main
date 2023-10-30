package com.knj.mirou.boundedContexts.reward;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Reward {
    @Id

    private long id;
    private long linkedChallengeId; //챌린지 아이디 연결
    private int round; //라운드
    private int rewardCoin; //보상 코인
}
