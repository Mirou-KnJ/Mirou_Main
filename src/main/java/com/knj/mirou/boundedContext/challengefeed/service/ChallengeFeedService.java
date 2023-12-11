package com.knj.mirou.boundedContext.challengefeed.service;

import com.knj.mirou.base.event.EventAfterWriteFeed;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeDetailDTO;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.AuthenticationMethod;
import com.knj.mirou.boundedContext.challengefeed.model.dtos.FeedListDTO;
import com.knj.mirou.boundedContext.challengefeed.model.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.model.enums.FeedStatus;
import com.knj.mirou.boundedContext.challengefeed.repository.ChallengeFeedRepository;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.coin.service.CoinService;
import com.knj.mirou.boundedContext.imageData.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final CoinService coinService;
    private final ApplicationEventPublisher publisher;

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

        ChallengeFeed savedFeed = challengeFeedRepository.save(feed);
        imageDataService.create(savedFeed.getId(), ImageTarget.FEED_IMG, imgUrl);

        publisher.publishEvent(new EventAfterWriteFeed(this, challengeMember));

        return RsData.of("S-1", "피드 작성에 성공했습니다.");
    }

    public ChallengeDetailDTO getDetailData(Challenge challenge, Member member, ChallengeDetailDTO detailDTO) {

        detailDTO.setCanWrite(alreadyPostedToday(member, challenge));

        List<ChallengeFeed> recently3Feed = getRecently3Feed(challenge);
        detailDTO.setRecently3Feeds(recently3Feed);

        return detailDTO;
    }

    public FeedListDTO getListDto(Challenge linkedChallenge, Member writer) {

        FeedListDTO feedListDto = new FeedListDTO();

        feedListDto.setLinkedChallenge(linkedChallenge);
        feedListDto.setChallengeImg(imageDataService.getOptimizingUrl(linkedChallenge.getImgUrl(),
                OptimizerOption.CHALLENGE_DETAIL));
        List<ChallengeFeed> feeds =
                challengeFeedRepository.findByLinkedChallengeAndStatus(linkedChallenge, FeedStatus.PUBLIC);
        List<ChallengeFeed> myFeeds =
                challengeFeedRepository.findByLinkedChallengeAndWriter(linkedChallenge, writer);

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

        Map<Long, String> feedListImages = imageDataService.getMyFeedListImages(myFeeds);
        feedListImages.putAll(imageDataService.getNotMineFeedListImages(notMineFeeds));

        return feedListImages;
    }

    @Transactional
    public void updateReportCount(ChallengeFeed feed) {

        feed.updateReportCount();
        int reportCount = feed.getReportCount();

        if(reportCount >= 5) {
            feed.updatePrivate();

            Member writer = feed.getWriter();
            Challenge linkedChallenge = feed.getLinkedChallenge();

            coinService.givePenalty(writer, linkedChallenge);
        }
    }
}
