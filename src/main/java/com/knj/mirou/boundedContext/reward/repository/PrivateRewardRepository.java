package com.knj.mirou.boundedContext.reward.repository;

import com.knj.mirou.boundedContext.reward.model.entity.PrivateReward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateRewardRepository extends JpaRepository<PrivateReward, Long> {
}
