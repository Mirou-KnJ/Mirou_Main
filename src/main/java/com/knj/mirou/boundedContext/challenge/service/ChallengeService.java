package com.knj.mirou.boundedContext.challenge.service;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengePeriod;
import com.knj.mirou.boundedContext.challenge.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {
    @Autowired
    private final ChallengeRepository challengeRepository;

    @Transactional
    public Challenge createChallenge(String name, String contents, String period) {

        Challenge newChallenge = Challenge.builder()
                .name(name)
                .contents(contents)
                .period(ChallengePeriod.valueOf(period))
                .build();

        return challengeRepository.save(newChallenge);
    }

    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }
}
