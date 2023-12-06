package com.knj.mirou.boundedContext.challenge.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeCreateDTO;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeDetailDTO;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.AuthenticationMethod;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeTag;
import com.knj.mirou.boundedContext.challenge.model.enums.MapCategory;
import com.knj.mirou.boundedContext.challenge.repository.ChallengeRepository;
import com.knj.mirou.boundedContext.challengefeed.model.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {

    private final ImageDataService imageDataService;
    private final ChallengeFeedService challengeFeedService;
    private final ChallengeMemberService challengeMemberService;

    private final ChallengeRepository challengeRepository;

    @Transactional
    public RsData<Long> create(ChallengeCreateDTO createDTO, String imgUrl) {

        if(!checkDeadLine(createDTO.getJoinDeadLine())) {
            return RsData.of("F-1", "유효하지 않은 참여 기한 입니다.");
        }

        if(!checkUniqueName(createDTO.getName())) {
            return RsData.of("F-2", "%s는 이미 사용 중인 챌린지 이름 입니다.".formatted(createDTO.getName()));
        }

        List<String> labels = new ArrayList<>();
        if(createDTO.getMethod().equals("PHOTO")) {
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
                .mapCategory(MapCategory.valueOf(createDTO.getPlaceCategory()))
                .level(createDTO.getLevel())
                .status(ChallengeStatus.BEFORE_SETTINGS)
                .precautions(createDTO.getPrecaution())
                .imgUrl(imgUrl)
                .build();

        Challenge challenge = challengeRepository.save(newChallenge);

        return RsData.of("S-1", "챌린지가 성공적으로 생성되었습니다.", challenge.getId());
    }

    public RsData<ChallengeDetailDTO> getDetailDTO(long challengeId, Member member) {

        Optional<Challenge> OChallenge = getById(challengeId);
        if(OChallenge.isEmpty()) {
            return RsData.of("F-1", "챌린지 정보를 찾을 수 없습니다.");
        }

        Challenge challenge = OChallenge.get();
        ChallengeDetailDTO detailDTO = new ChallengeDetailDTO();
        detailDTO.setChallenge(challenge);
        detailDTO.setLoginMember(member);

        detailDTO = challengeMemberService.getDetailData(challenge, member, detailDTO);
        detailDTO = challengeFeedService.getDetailData(challenge, member, detailDTO);

        List<String> imgList = new ArrayList<>();
        for (ChallengeFeed feed : detailDTO.getRecently3Feeds()) {
            imgList.add(imageDataService.getOptimizingUrl(feed.getImgUrl(), OptimizerOption.FEED_MODAL));
        }

        detailDTO.setDetailImg(imageDataService.getOptimizingUrl(
                challenge.getImgUrl(), OptimizerOption.CHALLENGE_DETAIL));

        detailDTO.setFeedOptimizedImages(imgList);

        return RsData.of("S-1", "디테일 정보가 성공적으로 수집되었습니다.", detailDTO);
    }

    public List<Challenge> getMyChallenges(Member member) {

        List<Challenge> inProgressChallenges = challengeMemberService.getInProgressChallenges(member);

        return inProgressChallenges;
    }

    public List<Challenge> getNotMineChallenges(Member member, List<Challenge> myProgressChallenges, String tag) {

        List<Challenge> openedChallenges;

        if(tag.equals("ALL")) {
            openedChallenges = getAllByStatus(ChallengeStatus.OPEN);
        } else {
            openedChallenges = getOpenedChallengeByTag(ChallengeTag.valueOf(tag));
        }

        List<Challenge> myChallenges = new ArrayList<>(myProgressChallenges);

        List<Challenge> myCompletedChallenges = getMyCompletedChallenges(member);
        if(!myChallenges.isEmpty()) {
            myChallenges.addAll(myCompletedChallenges);
        }

        for(Challenge myChallenge : myChallenges) {
            openedChallenges.remove(myChallenge);
        }

        sortChallengesByDate(openedChallenges);

        return openedChallenges;
    }

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

    public List<Challenge> getAllList() {
        return challengeRepository.findAll();
    }

    public List<AuthenticationMethod> getAllMethods() {
        return Arrays.asList(AuthenticationMethod.values());
    }

    public List<MapCategory> getAllCategories() {
        return Arrays.asList(MapCategory.values());
    }

    public List<Challenge> getAllByStatus(ChallengeStatus status) {
        return challengeRepository.findAllByStatus(status);
    }

    public Optional<Challenge> getById(long id) {
        return challengeRepository.findById(id);
    }

    public boolean checkDeadLine(LocalDate deadLine) {

        LocalDate today = LocalDate.now();
        if (deadLine.isBefore(today) || deadLine.isEqual(today)) {
            return false;
        }
        return true;
    }

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

    public List<Challenge> sortChallengesByDate(List<Challenge> challenges) {

        challenges.sort(Comparator.comparing(Challenge::getCreateDate).reversed());

        return challenges;
    }

    public List<Challenge> getMyCompletedChallenges(Member member){
        return challengeMemberService.getMyCompletedChallenges(member);
    }

    public List<Challenge> getOpenedChallengeByTag(ChallengeTag tag) {
        return challengeRepository.findByTagAndStatus(tag, ChallengeStatus.OPEN);
    }
}
