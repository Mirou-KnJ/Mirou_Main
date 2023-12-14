package com.knj.mirou.boundedContext.reward.repository;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.reward.model.entity.PrivateReward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivateRewardRepository extends JpaRepository<PrivateReward, Long> {

    List<PrivateReward> findByLinkedChallengeAndLinkedChallengeMember(Challenge challenge, ChallengeMember challengeMember);
}
