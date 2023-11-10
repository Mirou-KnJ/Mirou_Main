package com.knj.mirou.boundedContext.challengefeed.repository;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengefeed.entity.ChallengeFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeFeedRepository extends JpaRepository<ChallengeFeed, Long> {

    List<ChallengeFeed> findByLinkedChallenge(Challenge linkedChallenge);

}
