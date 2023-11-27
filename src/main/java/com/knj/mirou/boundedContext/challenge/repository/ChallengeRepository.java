package com.knj.mirou.boundedContext.challenge.repository;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    List<Challenge> findByStatus(ChallengeStatus status);

    Optional<Challenge> findByNameAndStatus(String name, ChallengeStatus challengeStatus);

    List<Challenge> findByJoinDeadlineAndStatus(LocalDate yesterday, ChallengeStatus status);

    List<Challenge> findByTagAndStatus(ChallengeTag tag, ChallengeStatus status);
}
