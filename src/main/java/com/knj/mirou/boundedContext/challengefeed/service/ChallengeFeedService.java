package com.knj.mirou.boundedContext.challengefeed.service;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengefeed.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.repository.ChallengeFeedRepository;
import com.knj.mirou.boundedContext.image.service.ImageService;
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
    private final ImageService imageService;
    private final ChallengeService challengeService;
    private final MemberService memberService;

    @Transactional
    public String writeFeed(long linkedChallengeId, String loginId, MultipartFile img) throws IOException {

        Member writer = memberService.getByLoginId(loginId).get();
        Challenge linkedChallenge = challengeService.getById(linkedChallengeId);

        String imgUrl = imageService.uploadImg(img);

        ChallengeFeed newFeed = ChallengeFeed.builder()
                .writer(writer)
                .linkedChallenge(linkedChallenge)
                .build();

        challengeFeedRepository.save(newFeed);

        return imageService.uploadImg(img);
    }

}
