package com.knj.mirou.boundedContext.challenge.service;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.AuthenticationMethod;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengePeriod;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeTag;
import com.knj.mirou.boundedContext.challenge.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    @Transactional
    public Challenge create(String name, String contents, int joinCost, LocalDate joinDeadLine,
                                     String period, String tag, String method, int level, String status, String precautions) {

        Challenge newChallenge = Challenge.builder()
                .name(name)
                .contents(contents)
                .joinCost(joinCost)
                .joinDeadline(joinDeadLine)
                .period(ChallengePeriod.valueOf(period))
                .tag(ChallengeTag.valueOf(tag))
                .method(AuthenticationMethod.valueOf(method))
                .level(level)
                .status(ChallengeStatus.valueOf(status))
                .precautions(precautions)
                .build();

        return challengeRepository.save(newChallenge);
    }

    public List<Challenge> allChallengeList() {
        return challengeRepository.findAll();
    }
}
