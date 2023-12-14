package com.knj.mirou.boundedContext.challengefeed.repository;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengefeed.model.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.model.enums.FeedStatus;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChallengeFeedRepository extends JpaRepository<ChallengeFeed, Long> {

    List<ChallengeFeed> findByLinkedChallengeAndStatus(Challenge linkedChallenge, FeedStatus status);

    @Query("SELECT cf FROM ChallengeFeed cf " +
            "WHERE cf.linkedChallenge = :challenge " +
            "AND cf.writer = :member " +
            "AND cf.createDate >= :startOfDay " +
            "AND cf.createDate <= :endOfDay")
    Optional<ChallengeFeed> findTodayLinkedChallengesForMember(
            @Param("challenge") Challenge challenge,
            @Param("member") Member member,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    List<ChallengeFeed> findTop3ByLinkedChallengeOrderByCreateDateAsc(Challenge linkedChallenge);

    List<ChallengeFeed> findByLinkedChallengeAndWriter(Challenge linkedChallenge, Member writer);

    int countByLinkedChallengeAndCreateDateBetween(Challenge linkedChallenge,
                                                   LocalDateTime startOfWeek, LocalDateTime endOfWeek);

    List<ChallengeFeed> findAllByWriterAndCreateDateBetween
            (Member writer, LocalDateTime startDayOfWeek, LocalDateTime endDayOfWeek);
}
