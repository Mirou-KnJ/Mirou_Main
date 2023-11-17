package com.knj.mirou.boundedContext.challenge.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeCreateDTO;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.AuthenticationMethod;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeTag;
import com.knj.mirou.boundedContext.challenge.repository.ChallengeRepository;
import com.knj.mirou.boundedContext.imageData.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {

    private final ImageDataService imageDataService;
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
        if (OById.isPresent()) {
            return OById.get();
        }

        return null;
    }

    @Transactional
    public RsData<Challenge> tryCreate(ChallengeCreateDTO createDTO, String imgUrl) {

        if(!checkDeadLine(createDTO.getJoinDeadLine())) {
            return RsData.of("F-1", "유효하지 않은 참여 기한(JoinDeadLine) 입니다.");
        }

        if(!checkUniqueName(createDTO.getName())) {
            return RsData.of("F-2", "%s는 이미 사용 중인 챌린지 이름 입니다.".formatted(createDTO.getName()));
        }

        Challenge newChallenge = Challenge.builder()
                .name(createDTO.getName())
                .contents(createDTO.getContents())
                .joinCost(createDTO.getJoinCost())
                .joinDeadline(createDTO.getJoinDeadLine())
                .period(createDTO.getPeriod())
                .tag(ChallengeTag.valueOf(createDTO.getTag()))
                .method(AuthenticationMethod.valueOf(createDTO.getMethod()))
                .level(createDTO.getLevel())
                .status(ChallengeStatus.BEFORE_SETTINGS)
                .precautions(createDTO.getPrecaution())
                .imgUrl(imgUrl)
                .build();

        Challenge challenge = challengeRepository.save(newChallenge);

        return RsData.of("S-1", "챌린지가 성공적으로 생성되었습니다.", challenge);
    }

    public boolean checkDeadLine(LocalDate deadLine) {

        LocalDate today = LocalDate.now();

        if (deadLine.isBefore(today) || deadLine.isEqual(today)) {
            return false;
        }

        return true;
    }

    //열려있거나, 세팅 이전으로 생성되어 있는 챌린지와 이름이 같아선 안된다.
    public boolean checkUniqueName(String name) {

        if(challengeRepository.findByNameAndStatus(name, ChallengeStatus.OPEN).isPresent()) {
            return false;
        }

        if(challengeRepository.findByNameAndStatus(name, ChallengeStatus.BEFORE_SETTINGS).isPresent()) {
            return false;
        }

        return true;
    }

    @Transactional
    public void opening(Challenge challenge) {

        challenge.openingChallenge();
        challengeRepository.save(challenge);
    }

}
