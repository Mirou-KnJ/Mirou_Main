package com.knj.mirou.boundedContext.challengefeed.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengefeed.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.repository.ChallengeFeedRepository;
import com.knj.mirou.boundedContext.imageData.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeFeedService {

    private final ChallengeFeedRepository challengeFeedRepository;
    private final ImageDataService imageDataService;
    private final ChallengeService challengeService;
    private final MemberService memberService;

    @Transactional
    public RsData<String> writeFeed(long linkedChallengeId, String loginId, MultipartFile img) throws IOException {

        Member writer = memberService.getByLoginId(loginId).get();
        Challenge linkedChallenge = challengeService.getById(linkedChallengeId);

        RsData<String> uploadRsData = imageDataService.uploadImg(img, ImageTarget.FEED_IMG);

        if(uploadRsData.isFail()) {
            return uploadRsData;
        }

        String imgUrl = uploadRsData.getData();

        /*
        TODO:챌린지 별로 라벨 검출 기준점을 어떻게 잡는 것이 좋을까? => 챌린지별로 어떤 라벨이 있어야 하는지 저장?
         예를 들어, 물 마시기 챌린지의 경우 water, bottle, drink 와 같은 기준 설정
        */
        RsData<String> labelRsData = imageDataService.detectLabelsGcs(imgUrl, linkedChallenge);

        if(labelRsData.isFail()) {
            return labelRsData;
        }

        RsData<String> safeSearchRsData = imageDataService.safeSearchByGcs(imgUrl);

        if(safeSearchRsData.isFail()) {
            return safeSearchRsData;
        }

        ChallengeFeed newFeed = ChallengeFeed.builder()
                .writer(writer)
                .linkedChallenge(linkedChallenge)
                .build();

        Long feedId = challengeFeedRepository.save(newFeed).getId();

        imageDataService.create(feedId, ImageTarget.FEED_IMG, imgUrl);

        return RsData.of("S-1", "피드 작성에 성공했습니다.");
    }

}
