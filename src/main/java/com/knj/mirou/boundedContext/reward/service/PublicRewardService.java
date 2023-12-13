package com.knj.mirou.boundedContext.reward.service;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.reward.model.entity.PublicReward;
import com.knj.mirou.boundedContext.reward.model.enums.RewardType;
import com.knj.mirou.boundedContext.reward.repository.PublicRewardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicRewardService {

    private final PublicRewardRepository publicRewardRepository;
    private final ChallengeService challengeService;

    @Transactional
    public void create(long challengeId, int round, String rewardType, String reward) {

        Optional<Challenge> OChallenge = challengeService.getById(challengeId);

        if(OChallenge.isEmpty()) {
            log.error("챌린지를 찾을 수 없습니다");
            return;
        }

        PublicReward newReward = PublicReward.builder()
                .linkedChallenge(OChallenge.get())
                .round(round)
                .rewardType(RewardType.valueOf(rewardType))
                .reward(reward)
                .build();

        publicRewardRepository.save(newReward);
    }

    @Transactional
    public void deleteReward(long rewardId){
        Optional<PublicReward> rewardToDelete = publicRewardRepository.findById(rewardId);
        if(rewardToDelete.isPresent()){
            publicRewardRepository.delete(rewardToDelete.get());
        }else{
            log.error("보상을 찾을 수 없습니다.");
        }
    }

    public List<PublicReward> getAllReward() {

        return publicRewardRepository.findAll();
    }

    public PublicReward getById(long publicRewardId) {

        return publicRewardRepository.findById(publicRewardId).get();
    }

}
