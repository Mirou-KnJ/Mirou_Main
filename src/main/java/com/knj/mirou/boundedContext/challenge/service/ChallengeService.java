package com.knj.mirou.boundedContext.challenge.service;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.AuthenticationMethod;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengePeriod;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeTag;
import com.knj.mirou.boundedContext.challenge.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    @Transactional
    public void createTest() {
        
        Challenge newChallenge = Challenge.builder()
                .name("임시 챌린지")
                .contents("임시로 생성한 챌린지 입니다")
                .joinDeadline(LocalDate.now())
                .period(ChallengePeriod.DAY7)
                .status(ChallengeStatus.OPEN)
                .tag(ChallengeTag.ROUTINE)
                .method(AuthenticationMethod.PHOTO)
                .level(5)
                .joinCost(1000)
                .precautions("임시 주의사항")
                .build();

        challengeRepository.save(newChallenge);
    }

    public List<Challenge> getAllList() {

        return challengeRepository.findAll();
    }

    public Challenge getById(long id) {

        Optional<Challenge> OById = challengeRepository.findById(id);

        if(OById.isPresent()) {
            return OById.get();
        }

        return null;
    }

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

    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

}
