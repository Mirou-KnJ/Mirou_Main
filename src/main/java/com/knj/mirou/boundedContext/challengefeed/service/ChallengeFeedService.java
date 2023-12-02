package com.knj.mirou.boundedContext.challengefeed.service;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeDetailDTO;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.AuthenticationMethod;
import com.knj.mirou.boundedContext.challengefeed.model.dtos.FeedListDTO;
import com.knj.mirou.boundedContext.challengefeed.model.entity.ChallengeFeed;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeFeedService {

    private final ImageDataService imageDataService;
    private final ChallengeMemberService challengeMemberService;
    private final PrivateRewardService privateRewardService;
    private final CoinService coinService;
    private final ChallengeFeedRepository challengeFeedRepository;

    @Transactional
    public RsData<String> write(Challenge challenge, Member member, MultipartFile img,
                                String contents) throws IOException {

        Optional<ChallengeMember> OChallengeMember = challengeMemberService.getByChallengeAndMember(challenge, member);
        if(OChallengeMember.isEmpty()) {
            return RsData.of("F-1", "챌린지 참여 정보가 올바르지 않습니다.");
        }
        ChallengeMember challengeMember = OChallengeMember.get();

        RsData<String> uploadRsData = imageDataService.tryUploadImg(img, ImageTarget.FEED_IMG);
        if(uploadRsData.isFail()) {
            return uploadRsData;
        }

        String imgUrl = uploadRsData.getData();

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
                .writer(member)
                .linkedChallenge(challenge)
                .contents(contents)
                .imgUrl(imgUrl)
                .build();

        ChallengeFeed saveFeed = challengeFeedRepository.save(feed);
        imageDataService.create(saveFeed.getId(), ImageTarget.FEED_IMG, imgUrl);

        return RsData.of("S-1", "피드 작성에 성공했습니다.");
    }

    @Transactional
    public void checkReward(Challenge challenge, Member member) {

        ChallengeMember challengeMember = challengeMemberService.getByChallengeAndMember(challenge, member).get();

        int successNum = challengeMemberService.updateSuccess(challengeMember);

        RsData<PrivateReward> validRewardRs =
                privateRewardService.getValidReward(challenge, challengeMember, successNum);

        if(validRewardRs.isSuccess()) {
            coinService.giveCoin(member, validRewardRs.getData(), challenge.getName(), challenge.getImgUrl());
        }

        if(validRewardRs.getResultCode().contains("S-2")) {
            challengeMemberService.finishChallenge(challengeMember);
        }
    }

    public ChallengeDetailDTO getDetailData(Challenge challenge, Member member, ChallengeDetailDTO detailDTO) {

        detailDTO.setCanWrite(alreadyPostedToday(member, challenge));

        List<ChallengeFeed> recently3Feed = getRecently3Feed(challenge);
        detailDTO.setRecently3Feeds(recently3Feed);

        return detailDTO;
    }

    public FeedListDTO getListDto(Challenge linkedChallenge, Member writer) {

        FeedListDTO feedListDto = new FeedListDTO();

        List<ChallengeFeed> feeds = challengeFeedRepository.findByLinkedChallenge(linkedChallenge);
        List<ChallengeFeed> myFeeds = challengeFeedRepository.findByLinkedChallengeAndWriter(linkedChallenge, writer);

        for(ChallengeFeed myFeed : myFeeds) {
            feeds.remove(myFeed);
        }

        feedListDto.setMyFeeds(myFeeds);
        feedListDto.setNotMineFeeds(feeds);

        return feedListDto;
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

    @Transactional
    public void updateLikeCount(ChallengeFeed challengeFeed) {
        challengeFeed.updateLikeCount();
    }

    public Map<Long, String> getFeedListImages(FeedListDTO feedListDTO) {

        List<ChallengeFeed> myFeeds = feedListDTO.getMyFeeds();
        List<ChallengeFeed> notMineFeeds = feedListDTO.getNotMineFeeds();

        Map<Long, String> feedListImages = imageDataService.getFeedListImages(myFeeds);
        feedListImages.putAll(imageDataService.getFeedListImages(notMineFeeds));

        return feedListImages;
    }
}
