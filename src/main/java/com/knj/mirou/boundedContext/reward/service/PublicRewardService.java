package com.knj.mirou.boundedContext.reward.service;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.reward.model.entity.PublicReward;
import com.knj.mirou.boundedContext.reward.model.enums.RewardType;
import com.knj.mirou.boundedContext.reward.repository.PublicRewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicRewardService {

    private final PublicRewardRepository publicRewardRepository;
    private final ChallengeService challengeService;

    @Transactional
    public void create(int challengeId, int round, String rewardType, String reward) {

        Challenge challengeById = challengeService.getById(challengeId);

        PublicReward newReward = PublicReward.builder()
                .linkedChallenge(challengeById)
                .round(round)
                .rewardType(RewardType.valueOf(rewardType))
                .reward(reward)
                .build();

        publicRewardRepository.save(newReward);
    }

    public List<PublicReward> getAllReward() {

        return publicRewardRepository.findAll();
    }

}
