package com.knj.mirou.boundedContext.challenge.repository;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus;
import com.knj.mirou.boundedContext.challengefeed.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    List<Challenge> findByStatus(ChallengeStatus status);

    Optional<Challenge> findByNameAndStatus(String name, ChallengeStatus challengeStatus);

    List<Challenge> findByJoinDeadlineAndStatus(LocalDate yesterday, ChallengeStatus status);
}
