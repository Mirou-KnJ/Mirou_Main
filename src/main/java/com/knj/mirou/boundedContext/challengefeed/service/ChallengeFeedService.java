package com.knj.mirou.boundedContext.challengefeed.service;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.AuthenticationMethod;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengefeed.model.dtos.FeedListDTO;
import com.knj.mirou.boundedContext.challengefeed.model.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.repository.ChallengeFeedRepository;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.coin.service.CoinService;
import com.knj.mirou.boundedContext.imageData.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import com.knj.mirou.boundedContext.reward.model.entity.PrivateReward;
import com.knj.mirou.boundedContext.reward.service.PrivateRewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeFeedService {

    private final Rq rq;
    private final ImageDataService imageDataService;
    private final ChallengeMemberService challengeMemberService;
    private final PrivateRewardService privateRewardService;
    private final CoinService coinService;
    private final MemberService memberService;
    private final ChallengeService challengeService;
    private final ChallengeFeedRepository challengeFeedRepository;

    public RsData<ChallengeMember> checkValidRequest(long challengeId, String loginId) {

        Optional<Member> OMember = memberService.getByLoginId(loginId);
        Optional<Challenge> OChallenge = challengeService.getById(challengeId);

        if(OMember.isEmpty() || OChallenge.isEmpty()) {
            return RsData.of("F-1", "챌린지 참여 상태가 올바르지 않습니다.");
        }

        Challenge challenge = OChallenge.get();
        Member loginMember = OMember.get();

        Optional<ChallengeMember> OChallengeMember =
                challengeMemberService.getByChallengeAndMember(challenge, loginMember);
        if(OChallengeMember.isEmpty()) {
            return RsData.of("F-2", "챌린지 참여 상태가 올바르지 않습니다.");
        }

        return RsData.of("S-1", "참여 정보가 확인되었습니다.", OChallengeMember.get());
    }

    @Transactional
    public RsData<String> tryWrite(ChallengeMember linkedChallengeMember, MultipartFile img,
                                   String contents) throws IOException {

        Member loginMember = linkedChallengeMember.getLinkedMember();
        Challenge challenge = linkedChallengeMember.getLinkedChallenge();

        RsData<String> uploadRsData = imageDataService.tryUploadImg(img, ImageTarget.FEED_IMG);
        if(uploadRsData.isFail()) {
            return uploadRsData;
        }

        String imgUrl = uploadRsData.getData();

        //라벨 검사는 인증샷 인증인 경우에만 수행
        if(challenge.getMethod().equals(AuthenticationMethod.PHOTO)) {
            RsData<String> labelRsData = imageDataService.detectLabelsGcs(imgUrl, challenge.getLabels());
            if(labelRsData.isFail()) {
                return labelRsData;
            }
        }
        
        RsData<String> safeSearchRsData = imageDataService.safeSearchByGcs(imgUrl);
        if(safeSearchRsData.isFail()) {
            return safeSearchRsData;
        }

        ChallengeFeed feed = ChallengeFeed.builder()
                .writer(linkedChallengeMember.getLinkedMember())
                .linkedChallenge(linkedChallengeMember.getLinkedChallenge())
                .contents(contents)
                .imgUrl(imgUrl)
                .build();

        ChallengeFeed saveFeed = challengeFeedRepository.save(feed);
        imageDataService.create(saveFeed.getId(), ImageTarget.FEED_IMG, imgUrl);

        int successNum = challengeMemberService.updateSuccess(linkedChallengeMember);

        RsData<PrivateReward> validRewardRs =
                privateRewardService.getValidReward(challenge, linkedChallengeMember, successNum);

        if(validRewardRs.isSuccess()) {
            coinService.giveCoin(loginMember, validRewardRs.getData());
        }

        if(validRewardRs.getResultCode().contains("S-2")) {
            challengeMemberService.finishChallenge(linkedChallengeMember);
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

    public Optional<ChallengeFeed> getById(long feedId) {

        return challengeFeedRepository.findById(feedId);
    }

    public List<ChallengeFeed> getRecently3Feed(Challenge linkedChallenge) {

        return challengeFeedRepository.findTop3ByLinkedChallengeOrderByCreateDateAsc(linkedChallenge);
    }

    public FeedListDTO getListDto(Challenge linkedChallenge) {

        FeedListDTO feedListDto = new FeedListDTO();

        Member writer = rq.getMember();

        List<ChallengeFeed> feeds = challengeFeedRepository.findByLinkedChallenge(linkedChallenge);
        List<ChallengeFeed> myFeeds = challengeFeedRepository.findByLinkedChallengeAndWriter(linkedChallenge, writer);

        for(ChallengeFeed myFeed : myFeeds) {
            feeds.remove(myFeed);
        }

        feedListDto.setMyFeeds(myFeeds);
        feedListDto.setNotMineFeeds(feeds);

        return feedListDto;
    }

    @Transactional
    public void updateLikeCount(ChallengeFeed challengeFeed) {
        challengeFeed.updateLikeCount();
    }

}
