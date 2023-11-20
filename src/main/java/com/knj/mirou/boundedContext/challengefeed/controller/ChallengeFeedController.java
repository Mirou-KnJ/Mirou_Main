package com.knj.mirou.boundedContext.challengefeed.controller;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengefeed.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.imageData.model.entity.ImageData;
import com.knj.mirou.boundedContext.imageData.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.service.MemberService;
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

    private final ChallengeService challengeService;
    private final ChallengeFeedService challengeFeedService;
    private final ImageDataService imageDataService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write/{id}")
    public String writeForm(@PathVariable(value = "id") long challengeId, Model model) {

        Optional<Challenge> OChallenge = challengeService.getById(challengeId);
        if(OChallenge.isEmpty()) {
            log.error("피드를 작성하려는 챌린지를 찾을 수 없습니다.");
            return "redirect:/";        //FIXME
        }
        Challenge challenge = OChallenge.get();

        //FIXME 챌린지 디테일이 아님.
        String challengeImg = imageDataService.getOptimizingUrl(challenge.getImgUrl(), OptimizerOption.CHALLENGE_DETAIL);

        model.addAttribute("challenge", challenge);
        model.addAttribute("challengeImg", challengeImg);

        return "view/challengeFeed/writeForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write/{id}")
    public String write(@PathVariable(value = "id") long challengeId, MultipartFile img,
                        String contents, Principal principal) throws IOException {

        String loginId = principal.getName();
        RsData<ChallengeMember> checkValidRs = challengeFeedService.checkValidRequest(challengeId, loginId);
        if(checkValidRs.isFail()) {
            checkValidRs.printResult();
            return "redirect:/";    //FIXME
        }

        RsData<String> writeRsData =
                challengeFeedService.tryWrite(checkValidRs.getData(), img, contents);
        if (writeRsData.isFail()) {
            writeRsData.printResult();
            return "redirect:/feed/write/" + challengeId;
        }

        return "redirect:/challenge/detail/" + challengeId;
    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable(value = "id") long feedId, Model model) {

        Optional<ChallengeFeed> OFeed = challengeFeedService.getById(feedId);
        if(OFeed.isEmpty()) {
            return "redirect:/";        //FIXME
        }
        ChallengeFeed feed = OFeed.get();

        //FIXME: 크기 조정된 이미지로 변경 필요
        ImageData feedImg = imageDataService.getByIdAndTarget(feedId, ImageTarget.FEED_IMG);

        model.addAttribute("feed", feed);
        model.addAttribute("feedImg", feedImg.getImageUrl());

        return "view/challengeFeed/detail";
    }

}
