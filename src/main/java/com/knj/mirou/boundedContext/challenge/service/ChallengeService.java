package com.knj.mirou.boundedContext.challenge.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeCreateDTO;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeDetailDTO;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.AuthenticationMethod;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeTag;
import com.knj.mirou.boundedContext.challenge.repository.ChallengeRepository;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {

    private final MemberService memberService;
    private final ChallengeMemberService challengeMemberService;
    private final ChallengeRepository challengeRepository;

    public List<Challenge> getAllList() {

        return challengeRepository.findAll();
    }

    public List<Challenge> getByStatus(ChallengeStatus status) {

        return challengeRepository.findByStatus(status);
    }

    public Optional<Challenge> getById(long id) {

        return challengeRepository.findById(id);
    }

    @Transactional
    public RsData<Challenge> tryCreate(ChallengeCreateDTO createDTO, String imgUrl) {

        List<String> labels = new ArrayList<>();

        if(!checkDeadLine(createDTO.getJoinDeadLine())) {
            return RsData.of("F-1", "유효하지 않은 참여 기한(JoinDeadLine) 입니다.");
        }

        if(!checkUniqueName(createDTO.getName())) {
            return RsData.of("F-2", "%s는 이미 사용 중인 챌린지 이름 입니다.".formatted(createDTO.getName()));
        }

        if(createDTO.getMethod().equals("PHOTO")) {
            log.info("Photo 입니다");
            labels = labelProcessing(createDTO.getLabelList());
        }

        Challenge newChallenge = Challenge.builder()
                .name(createDTO.getName())
                .contents(createDTO.getContents())
                .joinCost(createDTO.getJoinCost())
                .joinDeadline(createDTO.getJoinDeadLine())
                .period(createDTO.getPeriod())
                .tag(ChallengeTag.valueOf(createDTO.getTag()))
                .method(AuthenticationMethod.valueOf(createDTO.getMethod()))
                .labels(labels)
                .level(createDTO.getLevel())
                .status(ChallengeStatus.BEFORE_SETTINGS)
                .precautions(createDTO.getPrecaution())
                .imgUrl(imgUrl)
                .build();

        Challenge challenge = challengeRepository.save(newChallenge);

        return RsData.of("S-1", "챌린지가 성공적으로 생성되었습니다.", challenge);
    }

    public RsData<ChallengeDetailDTO> getDetailDTO(long challengeId, String loginId) {

        Optional<Member> OLoginMember = memberService.getByLoginId(loginId);
        if(OLoginMember.isEmpty()) {
            return RsData.of("F-1", "회원의 정보를 찾을 수 없습니다. 다시 로그인 해주세요.");
        }

        Optional<Challenge> OChallenge = getById(challengeId);
        if(OChallenge.isEmpty()) {
            return RsData.of("F-2", "유효한 챌린지가 아닙니다.");
        }

        Member member = OLoginMember.get();
        Challenge challenge = OChallenge.get();
        ChallengeDetailDTO detailDTO = new ChallengeDetailDTO();
        detailDTO.setChallenge(challenge);
        detailDTO.setLoginMember(member);

        Optional<ChallengeMember> OChallengeMember = challengeMemberService.getByChallengeAndMember(challenge, member);
        detailDTO.setJoin(OChallengeMember.isPresent());

        ChallengeMember challengeMember;
        if(!detailDTO.isJoin()) {
            detailDTO.setPublicRewards(challenge.getPublicReward());
        } else {
            challengeMember = OChallengeMember.get();
            detailDTO.setPrivateRewards(challengeMember.getPrivateReward());
        }

        detailDTO.setCanJoin(challengeMemberService.canJoin(member));
        detailDTO.setMemberCount(challengeMemberService.getCountByLinkedChallenge(challenge));

        return RsData.of("S-1", "디테일 정보가 성공적으로 수집되었습니다.", detailDTO);
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

    public List<String> labelProcessing(String labels) {

        List<String> labelList = new ArrayList<>();
        String[] splitLabels = labels.split(",");
        for(String label : splitLabels) {
            labelList.add(label);
        }

        return labelList;
    }

    //초 분 시 일 월 요일 년도
    //매일 0시 0분 3초에 어제까지가 기한이었던 챌린지들을 종료시킴.
    @Transactional
    @Scheduled(cron= "3 0 0 * * ?")
    public void getEndTargetList() {

        LocalDate yesterDay = LocalDate.now().minusDays(1);
        List<Challenge> endTargetChallenges =
                challengeRepository.findByJoinDeadlineAndStatus(yesterDay, ChallengeStatus.OPEN);

        if(!endTargetChallenges.isEmpty()) {
            for(Challenge endTarget : endTargetChallenges) {
                endTarget.closingChallenge();
                log.info(endTarget.getName() + "챌린지가 종료 처리 되었습니다.");
            }
        }
    }
    public List<Challenge> getMyValidChallengeList(String loginId) {

        return challengeMemberService.getMyValidChallengeList(loginId);
    }

    public List<Challenge> getNotMineOpenedChallenge(List<Challenge> myChallenges, List<Challenge> openedChallenge) {

        for(Challenge myChallenge : myChallenges) {
            openedChallenge.remove(myChallenge);
        }

        return openedChallenge;
    }

}
