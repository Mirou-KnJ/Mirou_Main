package com.knj.mirou.boundedContext.challengefeed.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengefeed.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.repository.ChallengeFeedRepository;
import com.knj.mirou.boundedContext.imageData.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeFeedService {

    private final ChallengeFeedRepository challengeFeedRepository;
    private final ImageDataService imageDataService;

    @Transactional
    public RsData<String> writeFeed(Challenge challenge, Member loginedMember, MultipartFile img,
                                    String contents) throws IOException {

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

        ChallengeFeed newFeed = ChallengeFeed.builder()
                .writer(loginedMember)
                .linkedChallenge(challenge)
                .contents(contents)
                .build();

        Long feedId = challengeFeedRepository.save(newFeed).getId();

        imageDataService.create(feedId, ImageTarget.FEED_IMG, imgUrl);

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
