package com.knj.mirou.boundedContext.reward.service;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.member.model.entity.Member;
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

}
