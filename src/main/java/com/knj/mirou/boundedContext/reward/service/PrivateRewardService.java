package com.knj.mirou.boundedContext.reward.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.reward.model.entity.PrivateReward;
import com.knj.mirou.boundedContext.reward.model.entity.PublicReward;
import com.knj.mirou.boundedContext.reward.repository.PrivateRewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PrivateRewardService {

    private final PrivateRewardRepository privateRewardRepository;

    @Transactional
    public void create(Challenge challenge, ChallengeMember challengeMember) {

        List<PublicReward> publicRewards = challenge.getPublicReward();

        for(PublicReward reward : publicRewards) {
            PrivateReward privateReward = PrivateReward.builder()
                    .linkedChallenge(challenge)
                    .linkedChallengeMember(challengeMember)
                    .round(reward.getRound())
                    .reward(reward.getReward())
                    .rewardType(reward.getRewardType())
                    .build();

            privateRewardRepository.save(privateReward);
        }
    }

    public RsData<PrivateReward> getValidReward(Challenge challenge, ChallengeMember challengeMember, int successNum) {

        List<PrivateReward> rewards = privateRewardRepository
                .findByLinkedChallengeAndLinkedChallengeMember(challenge, challengeMember);

        PrivateReward validReward = null;
        boolean lastFlag = false;

        for(PrivateReward reward : rewards) {
            if (reward.getRound() == successNum) {
                validReward = reward;
                if(rewards.get(rewards.size()-1).equals(validReward)) {
                    lastFlag = true;
                }
                break;
            }
        }

        if(validReward == null) {
            return RsData.of("F-1", "받을 수 있는 보상이 없습니다.");
        }

        if(!lastFlag ) {
            return RsData.of("S-1", "이번 회차 보상이 존재합니다", validReward);
        }

        return RsData.of("S-2", "마지막 회차 보상입니다.", validReward);
    }

}
