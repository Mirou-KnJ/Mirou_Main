package com.knj.mirou.boundedContext.reward.repository;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.reward.model.entity.PublicReward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PublicRewardRepository extends JpaRepository<PublicReward, Long> {

    Optional<PublicReward> findByLinkedChallengeAndRound(Challenge linkedChallenge, int round);
}
