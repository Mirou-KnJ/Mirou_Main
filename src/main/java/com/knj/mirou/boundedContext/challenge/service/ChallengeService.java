package com.knj.mirou.boundedContext.challenge.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.AuthenticationMethod;
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

    public List<Challenge> getAllList() {

        return challengeRepository.findAll();
    }

    public List<Challenge> getByStatus(ChallengeStatus status) {

        return challengeRepository.findByStatus(status);
    }

    public Challenge getById(long id) {

        Optional<Challenge> OById = challengeRepository.findById(id);

        //FIXME : Present와 Empty시의 동작 구분 확실히.
        if(OById.isPresent()) {
            return OById.get();
        }

        return null;
    }

    @Transactional
    public RsData<Challenge> create(String name, String contents, int joinCost, LocalDate joinDeadLine,
                                 int period, String tag, String method, int level, String precautions) {

        //TODO: 챌린지 생성 유효성 검사 ex) 이름 중복 불가, 비어있는 내용 불가 등

        Challenge newChallenge = Challenge.builder()
                .name(name)
                .contents(contents)
                .joinCost(joinCost)
                .joinDeadline(joinDeadLine)
                .period(period)
                .tag(ChallengeTag.valueOf(tag))
                .method(AuthenticationMethod.valueOf(method))
                .level(level)
                .status(ChallengeStatus.BEFORE_SETTINGS)
                .precautions(precautions)
                .build();

        Challenge challenge = challengeRepository.save(newChallenge);

        return RsData.of("S-1", "챌린지가 생성되었습니다.", challenge);
    }

}
