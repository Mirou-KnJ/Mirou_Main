package com.knj.mirou.boundedContext.challengefeed.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.config.MapConfigProperties;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.AuthenticationMethod;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengefeed.model.dtos.FeedListDTO;
import com.knj.mirou.boundedContext.challengefeed.model.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/feed")
public class ChallengeFeedController {

    private final Rq rq;
    private final ChallengeService challengeService;
    private final ChallengeFeedService challengeFeedService;
    private final ImageDataService imageDataService;
    private final MapConfigProperties mapConfigProps;

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

        if(challenge.getMethod().equals(AuthenticationMethod.LOCATION)) {

            String mapKey = mapConfigProps.getKey();
            model.addAttribute("mapKey", mapKey);
            model.addAttribute("category", challenge.getMapCategory());

            return "view/challengeFeed/mapWriteForm";
        }

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list/{id}")
    public String showList(@PathVariable(value = "id") long challengeId, Model model) {

        Optional<Challenge> OChallenge = challengeService.getById(challengeId);
        if(OChallenge.isEmpty()){

            log.error("NONE CHALLENGE ERROR");

            return rq.historyBack("대상 챌린지를 찾을 수 없습니다.");
        }

        FeedListDTO feedListDto = challengeFeedService.getListDto(OChallenge.get());

        model.addAttribute("feedListDto", feedListDto);

        //FIXME: 임시
        model.addAttribute("ImageDataService", imageDataService);
        model.addAttribute("option", OptimizerOption.FEED_MODAL);

        return "view/challengeFeed/list";
    }

    @ResponseBody
    @PostMapping("/like/{id}")
    public void like(@PathVariable(value = "id") long feedId) {

        Optional<ChallengeFeed> OFeed = challengeFeedService.getById(feedId);
        if(OFeed.isEmpty()) {
            return;
        }

        //FIXME: 새로고침만 된다면 좋아요는 몇번이고 누를 수 있으며, 좋아한 내용이 저장되지 않는 단순한 상황.
        ChallengeFeed challengeFeed = OFeed.get();

        challengeFeedService.updateLikeCount(challengeFeed);

        log.info("좋아요 수 : " + challengeFeed.getLikeCount());
    }

}
