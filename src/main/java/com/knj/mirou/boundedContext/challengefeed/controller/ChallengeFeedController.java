package com.knj.mirou.boundedContext.challengefeed.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.config.MapConfigProperties;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.AuthenticationMethod;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengefeed.model.dtos.FeedListDTO;
import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/feed")
public class ChallengeFeedController {

    private final Rq rq;
    private final MapConfigProperties mapConfigProps;

    private final ImageDataService imageDataService;
    private final ChallengeService challengeService;
    private final ChallengeFeedService challengeFeedService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write/{id}")
    public String writeForm(@PathVariable(value = "id") long challengeId, Model model) {

        Optional<Challenge> OChallenge = challengeService.getById(challengeId);
        if (OChallenge.isEmpty()) {
            return rq.historyBack("챌린지 정보를 찾을 수 없습니다.");
        }

        Challenge challenge = OChallenge.get();
        String challengeImg = imageDataService.getOptimizingUrl(challenge.getImgUrl(), OptimizerOption.CHALLENGE_DETAIL);

        model.addAttribute("challenge", challenge);
        model.addAttribute("challengeImg", challengeImg);

        if (challenge.getMethod().equals(AuthenticationMethod.LOCATION)) {

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
                        String contents) throws IOException {

        Member member = rq.getMember();
        Optional<Challenge> OChallenge = challengeService.getById(challengeId);
        if (OChallenge.isEmpty()) {
            return rq.historyBack("챌린지 정보가 유효하지 않습니다.");
        }

        Challenge challenge = OChallenge.get();

        RsData<String> writeRsData =
                challengeFeedService.write(challenge, member, img, contents);
        if (writeRsData.isFail()) {
            return rq.historyBack(writeRsData);
        }

        return rq.redirectWithMsg("/challenge/detail/" + challengeId, writeRsData);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list/{id}")
    public String showList(@PathVariable(value = "id") long challengeId, Model model) {

        Optional<Challenge> OChallenge = challengeService.getById(challengeId);
        if (OChallenge.isEmpty()) {
            return rq.historyBack("대상 챌린지를 찾을 수 없습니다.");
        }

        Member member = rq.getMember();

        FeedListDTO feedListDto = challengeFeedService.getListDto(OChallenge.get(), member);
        Map<Long, String> feedListImages = challengeFeedService.getFeedListImages(feedListDto);

        model.addAttribute("feedListDto", feedListDto);
        model.addAttribute("feedImages", feedListImages);

        return "view/challengeFeed/list";
    }

    @ResponseBody
    @PostMapping("/like")
    public ResponseEntity<RsData<Integer>> like(@RequestParam Map<String, Object> params) {

        long feedId = Long.parseLong(params.get("feedId").toString());

        Member member = rq.getMember();
        RsData<Integer> likeRs = challengeFeedService.updateLikeCount(feedId, member);

        return ResponseEntity.ok(likeRs);
    }

    @ResponseBody
    @PostMapping("/hide")
    public ResponseEntity<RsData<String>> hide(@RequestParam Map<String, Object> params) {

        Member member = rq.getMember();
        long feedId = Long.parseLong(params.get("feedId").toString());

        RsData<String> hideRs = challengeFeedService.hideFeed(member, feedId);

        return ResponseEntity.ok(hideRs);
    }
}
