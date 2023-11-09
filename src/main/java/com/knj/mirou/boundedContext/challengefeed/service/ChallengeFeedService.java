package com.knj.mirou.boundedContext.challengefeed.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengefeed.repository.ChallengeFeedRepository;
import com.knj.mirou.boundedContext.image.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.image.service.ImageService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeFeedService {

    private final ChallengeFeedRepository challengeFeedRepository;
    private final ImageService imageService;
    private final ChallengeService challengeService;
    private final MemberService memberService;

    @Transactional
    public RsData<String> writeFeed(long linkedChallengeId, String loginId, MultipartFile img) throws IOException {

        Member writer = memberService.getByLoginId(loginId).get();
        Challenge linkedChallenge = challengeService.getById(linkedChallengeId);

        RsData<String> uploadRsData = imageService.uploadImg(img, ImageTarget.FEED_IMG);

        if(uploadRsData.isFail()) {
            return uploadRsData;
        }


//        ChallengeFeed newFeed = ChallengeFeed.builder()
//                .writer(writer)
//                .linkedChallenge(linkedChallenge)
//                .build();
//
//        challengeFeedRepository.save(newFeed);
//
//        return imageService.uploadImg(img);

        return RsData.of("S-1", "피드 작성에 성공했습니다.");
    }

}
