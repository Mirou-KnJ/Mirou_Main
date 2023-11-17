package com.knj.mirou.boundedContext.challengefeed.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengefeed.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.repository.ChallengeFeedRepository;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.coin.service.CoinService;
import com.knj.mirou.boundedContext.imageData.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.reward.model.entity.PrivateReward;
import com.knj.mirou.boundedContext.reward.service.PrivateRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeFeedService {

    private final ChallengeFeedRepository challengeFeedRepository;
    private final ImageDataService imageDataService;
    private final ChallengeMemberService challengeMemberService;
    private final PrivateRewardService privateRewardService;
    private final CoinService coinService;

    @Transactional
    public RsData<String> tryWrite(Challenge challenge, Member loginedMember, MultipartFile img,
                                    String contents) throws IOException {

        Optional<ChallengeMember> OChallengeMember =
                challengeMemberService.getByChallengeAndMember(challenge, loginedMember);
        if(OChallengeMember.isEmpty()) {
            return RsData.of("F-1", "챌린지 참여 정보가 유효하지 않습니다.");
        }

        RsData<String> uploadRsData = imageDataService.tryUploadImg(img, ImageTarget.FEED_IMG);
        if(uploadRsData.isFail()) {
            return uploadRsData;
        }

        String imgUrl = uploadRsData.getData();

        /*
        TODO:챌린지 별로 라벨 검출 기준점을 어떻게 잡는 것이 좋을까? => 챌린지별로 어떤 라벨이 있어야 하는지 저장?
         예를 들어, 물 마시기 챌린지의 경우 water, bottle, drink 와 같은 기준 설정
        */
        RsData<String> labelRsData = imageDataService.detectLabelsGcs(imgUrl, challenge);
        if(labelRsData.isFail()) {
            return labelRsData;
        }

        RsData<String> safeSearchRsData = imageDataService.safeSearchByGcs(imgUrl);
        if(safeSearchRsData.isFail()) {
            return safeSearchRsData;
        }

        ChallengeFeed feed = ChallengeFeed.builder()
                .writer(loginedMember)
                .linkedChallenge(challenge)
                .contents(contents)
                .imgUrl(imgUrl)
                .build();

        ChallengeFeed saveFeed = challengeFeedRepository.save(feed);
        imageDataService.create(saveFeed.getId(), ImageTarget.FEED_IMG, imgUrl);

        ChallengeMember linkedChallengeMember = OChallengeMember.get();

        int successNum = challengeMemberService.updateSuccess(linkedChallengeMember);

        RsData<PrivateReward> validRewardRs =
                privateRewardService.getValidReward(challenge, linkedChallengeMember, successNum);

        if(validRewardRs.getResultCode().startsWith("S-2")) {
            challengeMemberService.finishChallenge(linkedChallengeMember);
            coinService.giveCoin(loginedMember, validRewardRs.getData());
        } else if(validRewardRs.isSuccess()) {
            coinService.giveCoin(loginedMember, validRewardRs.getData());
        }

        return RsData.of("S-1", "피드 작성에 성공했습니다.");
    }

    public boolean alreadyPostedToday(Member member, Challenge challenge){

        LocalDate today = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalTime endTime = LocalTime.of(23,59);

        LocalDateTime startDate = LocalDateTime.of(today, startTime);
        LocalDateTime endDate = LocalDateTime.of(today, endTime);

        return challengeFeedRepository
                .findTodayLinkedChallengesForMember(challenge, member, startDate, endDate).isPresent();
    }

    public ChallengeFeed getById(long feedId) {

        //FIXME: isPresent 없는 get 호출
        return challengeFeedRepository.findById(feedId).get();
    }

    public List<ChallengeFeed> getByChallenge(Challenge linkedChallenge) {

        return challengeFeedRepository.findByLinkedChallenge(linkedChallenge);
    }

    public List<ChallengeFeed> getRecently3Feed(Challenge linkedChallenge) {

        return challengeFeedRepository.findTop3ByLinkedChallengeOrderByCreateDateAsc(linkedChallenge);
    }

}
