package com.knj.mirou.boundedContext.reward.repository;

import com.knj.mirou.boundedContext.reward.model.entity.PublicReward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicRewardRepository extends JpaRepository<PublicReward, Long> {
}
