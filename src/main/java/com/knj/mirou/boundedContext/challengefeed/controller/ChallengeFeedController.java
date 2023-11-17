package com.knj.mirou.boundedContext.challengefeed.controller;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengefeed.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.coin.service.CoinService;
import com.knj.mirou.boundedContext.imageData.model.entity.ImageData;
import com.knj.mirou.boundedContext.imageData.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import com.knj.mirou.boundedContext.reward.model.entity.PrivateReward;
import com.knj.mirou.boundedContext.reward.service.PrivateRewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/feed")
public class ChallengeFeedController {

    private final MemberService memberService;
    private final ChallengeService challengeService;
    private final ChallengeFeedService challengeFeedService;
    private final ChallengeMemberService challengeMemberService;
    private final ImageDataService imageDataService;
    private final PrivateRewardService privateRewardService;
    private final CoinService coinService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write/{id}")
    public String writeForm(@PathVariable(value = "id") long challengeId, Model model, Principal principal) {


        //FIXME
        Challenge challenge = challengeService.getById(challengeId).get();
        ImageData challengeImageData = imageDataService.getByIdAndTarget(challengeId, ImageTarget.CHALLENGE_IMG);
        String challengeImg;

        if(challengeImageData == null) {
            challengeImg = imageDataService
                    .getOptimizingUrl("https://kr.object.ncloudstorage.com/mirou/etc/no_img.png"
                            , OptimizerOption.CHALLENGE_DETAIL);
        } else {
            challengeImg = imageDataService
                    .getOptimizingUrl(challengeImageData.getImageUrl(), OptimizerOption.CHALLENGE_DETAIL);
        }

        model.addAttribute("challenge", challenge);
        model.addAttribute("challengeImg", challengeImg);

        return "view/challengeFeed/writeForm";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write/{id}")
    public String write(@PathVariable(value = "id") long challengeId, MultipartFile img,
                        String contents, Principal principal) throws IOException {

        Member loginedMember = memberService.getByLoginId(principal.getName()).get();
        Optional<Challenge> OChallenge = challengeService.getById(challengeId);
        if(OChallenge.isEmpty()){
            log.error("대상 챌린지를 찾을 수 없습니다.");
            return "redirect:/feed/write" + challengeId;
        }

        Challenge challenge = OChallenge.get();

        RsData<String> writeRsData = challengeFeedService.writeFeed(challenge, loginedMember, img, contents);

        //TODO: 반드시 구조개선
        if (writeRsData.isFail()) {
            writeRsData.printResult();
            return "redirect:/feed/write/" + challengeId;
        } else {
            ChallengeMember challengeMember =
                    challengeMemberService.getByChallengeAndMember(challenge, loginedMember).get();

            int successNum = challengeMemberService.updateSuccess(challengeMember);
            RsData<PrivateReward> validReward =
                    privateRewardService.getValidReward(challenge, challengeMember, successNum);

            validReward.printResult();

            if(validReward.getResultCode().startsWith("S-2")) {
                challengeMemberService.finishChallenge(challengeMember);
                coinService.giveCoin(loginedMember, validReward.getData());
            } else if(validReward.isSuccess()) {
                coinService.giveCoin(loginedMember, validReward.getData());
            }

            writeRsData.printResult();
        }

        return "redirect:/challenge/detail/" + challengeId;
    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable(value = "id") long feedId, Model model) {

        ChallengeFeed feed = challengeFeedService.getById(feedId);
        ImageData feedImg = imageDataService.getByIdAndTarget(feedId, ImageTarget.FEED_IMG);

        model.addAttribute("feed", feed);
        model.addAttribute("feedImg", feedImg.getImageUrl());

        return "view/challengeFeed/detail";
    }

}
