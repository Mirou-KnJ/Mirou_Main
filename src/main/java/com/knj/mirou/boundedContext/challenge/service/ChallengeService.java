package com.knj.mirou.boundedContext.challenge.service;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.AuthenticationMethod;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengePeriod;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeTag;
import com.knj.mirou.boundedContext.challenge.repository.ChallengeRepository;
import com.knj.mirou.boundedContext.reward.model.entity.PublicReward;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
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
    public Challenge createTest() {
        
        Challenge newChallenge = Challenge.builder()
                .name("임시 챌린지")
                .contents("임시로 생성한 챌린지 입니다")
                .joinDeadline(LocalDate.now())
                .period(ChallengePeriod.DAY7)
                .status(ChallengeStatus.OPEN)
                .tag(ChallengeTag.ROUTINE)
                .method(AuthenticationMethod.PHOTO)
                .requiredNum(3)
                .level(5)
                .joinCost(1000)
                .precautions("임시 주의사항")
                .build();

        return challengeRepository.save(newChallenge);
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

    @Transactional
    public void updateReward(long linkedChallengeId, PublicReward reward) {

        Challenge challengeById = getById(linkedChallengeId);

        challengeById.updateReward(reward);
    }

}
