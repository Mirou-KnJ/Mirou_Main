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
import java.time.temporal.ChronoUnit;
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
    private final CoinService coinService;
    private final ApplicationEventPublisher publisher;

    private final ChallengeFeedRepository challengeFeedRepository;

    @Transactional
    public RsData<String> write(Challenge challenge, Member member, MultipartFile img,
                                String contents) throws IOException {

        Optional<ChallengeMember> OChallengeMember = challengeMemberService.getByChallengeAndMember(challenge, member);
        if (OChallengeMember.isEmpty()) {
            return RsData.of("F-1", "챌린지 참여 정보가 올바르지 않습니다.");
        }
        ChallengeMember challengeMember = OChallengeMember.get();

        RsData<String> uploadRsData = imageDataService.tryUploadImg(img, ImageTarget.FEED_IMG);
        if (uploadRsData.isFail()) {
            return uploadRsData;
        }

        String imgUrl = uploadRsData.getData();

        if (challenge.getMethod().equals(AuthenticationMethod.PHOTO)) {
            RsData<String> labelRsData = imageDataService.detectLabelsGcs(imgUrl, challenge.getLabels());
            if (labelRsData.isFail()) {
                return labelRsData;
            }
        }

        RsData<String> safeSearchRsData = imageDataService.safeSearchByGcs(imgUrl);
        if (safeSearchRsData.isFail()) {
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

        for (ChallengeFeed myFeed : myFeeds) {
            feeds.remove(myFeed);
        }

        feedListDto.setMyFeeds(myFeeds);
        feedListDto.setNotMineFeeds(feeds);

        return feedListDto;
    }

    public boolean alreadyPostedToday(Member member, Challenge challenge) {

        LocalDate today = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalTime endTime = LocalTime.of(23, 59);

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
    public RsData<Integer> updateLikeCount(long feedId, Member member) {

        Optional<ChallengeFeed> OFeed = getById(feedId);
        if (OFeed.isEmpty()) {
            return RsData.of("F-1", "피드 정보를 확인할 수 없습니다.");
        }

        ChallengeFeed challengeFeed = OFeed.get();
        if (challengeFeed.getWriter().equals(member)) {
            return RsData.of("F-2", "자신의 글은 좋아요를 누를 수 없습니다.");
        }

        challengeFeed.updateLikeCount();

        return RsData.of("S-1", "좋아요가 반영되었습니다.", challengeFeed.getLikeCount());
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

        if (reportCount >= 5) {
            feed.updatePrivate();

            Member writer = feed.getWriter();
            Challenge linkedChallenge = feed.getLinkedChallenge();

            coinService.givePenalty(writer, linkedChallenge);
        }
    }

    public int getWeeklyLikeCounts(Member member) {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startDayOfWeek = now.minus(7,
                ChronoUnit.DAYS).withHour(0).withMinute(0).withSecond(0);

        LocalDateTime endDayOfWeek = now.minus(1,
                ChronoUnit.DAYS).withHour(23).withMinute(59).withSecond(59);

        List<ChallengeFeed> weeklyFeeds = challengeFeedRepository
                .findAllByWriterAndCreateDateBetween(member, startDayOfWeek, endDayOfWeek);

        int likeCount = 0;

        for (ChallengeFeed feed : weeklyFeeds) {
            likeCount += feed.getLikeCount();
        }

        return likeCount;
    }

    public Map<Long, Integer> getWeeklyWriteCounts(List<Challenge> challenges) {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startDayOfWeek = now.minus(6,
                ChronoUnit.DAYS).withHour(0).withMinute(0).withSecond(0);

        Map<Long, Integer> writeCounts = new HashMap<>();

        for (Challenge challenge : challenges) {
            int count = challengeFeedRepository
                    .countByLinkedChallengeAndCreateDateBetween(challenge, startDayOfWeek, now);
            writeCounts.put(challenge.getId(), count);
        }

        return writeCounts;
    }

    @Transactional
    public RsData<String> hideFeed(Member member, long feedId) {

        Optional<ChallengeFeed> OFeed = getById(feedId);
        if (OFeed.isEmpty()) {
            return RsData.of("F-1", "피드 정보가 유효하지 않습니다.");
        }

        ChallengeFeed challengeFeed = OFeed.get();
        if (!challengeFeed.getWriter().equals(member)) {
            return RsData.of("F-2", "내 피드만 숨김처리 할 수 있습니다.");
        }

        if (challengeFeed.getStatus().equals(FeedStatus.PRIVATE)) {
            return RsData.of("F-3", "이미 숨김처리된 피드입니다.");
        }

        challengeFeed.updatePrivate();

        return RsData.of("S-1", "숨김 처리가 완료되었습니다.");
    }
}
