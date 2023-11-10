package com.knj.mirou.boundedContext.challenge.controller;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengefeed.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.imageData.model.entity.ImageData;
import com.knj.mirou.boundedContext.imageData.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
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
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final MemberService memberService;
    private final ChallengeMemberService challengeMemberService;
    private final ChallengeFeedService challengeFeedService;
    private final ImageDataService imageDataService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/create")
    public String createForm() {

        return "/view/challenge/createForm";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String createChallenge(String name, String contents, MultipartFile img, int joinCost, LocalDate joinDeadLine,
                                  int period, String tag, String method, int level, String precaution) throws IOException {

        RsData<Challenge> createRsData =
                challengeService.create(name, contents, joinCost, joinDeadLine, period, tag, method, level, precaution);

        if (createRsData.isFail()) {
            //TODO : 오류 원인 반환 후 다시 생성페이지로 리다이렉트
            return "redirect:/challenge/create";
        }

        long challengeId = createRsData.getData().getId();
        RsData<String> uploadRsData = imageDataService.uploadImg(img, ImageTarget.CHALLENGE_IMG);

        if (uploadRsData.isFail()) {
            //TODO : 오류 원인 반환 후 다시 생성페이지로 리다이렉트
            return "redirect:/challenge/create";
        }

        imageDataService.create(challengeId, ImageTarget.CHALLENGE_IMG, uploadRsData.getData());

        return "redirect:/challenge/detail/" + challengeId;
    }

    @GetMapping("/allChallengeList")
    public String allChallengeList(Model model) {
        model.addAttribute("challengeList", challengeService.getAllList());
        return "view/challenge/allChallengeList";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable(value = "id") long challengeId, Principal principal, Model model) {

        Member loginedMember = memberService.getByLoginId(principal.getName()).get();
        Challenge challenge = challengeService.getById(challengeId);
        List<ChallengeFeed> feedList = challengeFeedService.getByChallenge(challenge);
        ImageData challengeImg = imageDataService.getByIdAndTarget(challenge.getId(), ImageTarget.CHALLENGE_IMG);

        //TODO: 구조 개선
        if (challengeMemberService.getByChallengeAndMember(challenge, loginedMember).isPresent()) {
            model.addAttribute("isJoin", true);
        } else {
            model.addAttribute("isJoin", false);
        }

        if (challengeImg != null) {
            model.addAttribute("challengeImg",
                    imageDataService.getOptimizingUrl(challengeImg, OptimizerOption.CHALLENGE_DETAIL));
        } else {
            model.addAttribute("challengeImg", null);
        }

        model.addAttribute("challenge", challenge);
        model.addAttribute("feedList", feedList);

        return "view/challenge/detail";
    }

}
