package com.knj.mirou.boundedContext.reward.service;

import com.knj.mirou.base.rsData.RsData;
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
    public RsData<Long> create(long challengeId, int round, String rewardType, String reward) {

        Optional<Challenge> OChallenge = challengeService.getById(challengeId);
        if(OChallenge.isEmpty()) {
            return RsData.of("F-1", "챌린지를 찾을 수 없습니다.");
        }
        Challenge challenge = OChallenge.get();

        Optional<PublicReward> OPublicReward = publicRewardRepository.findByLinkedChallengeAndRound(challenge, round);
        if(OPublicReward.isPresent()) {
            return update(OPublicReward.get(), reward);
        }

        PublicReward newReward = PublicReward.builder()
                .linkedChallenge(OChallenge.get())
                .round(round)
                .rewardType(RewardType.valueOf(rewardType))
                .reward(reward)
                .build();

        publicRewardRepository.save(newReward);

        return RsData.of("S-1", "새로운 보상이 설정되었습니다.");
    }

    @Transactional
    public RsData<Long> update(PublicReward publicReward, String reward){
        publicReward.changeReward(reward);

        return RsData.of("S-2", "기존 보상이 수정되었습니다.", publicReward.getId());
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
