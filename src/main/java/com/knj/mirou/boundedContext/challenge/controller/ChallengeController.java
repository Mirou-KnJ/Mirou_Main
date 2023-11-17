package com.knj.mirou.boundedContext.challenge.controller;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeCreateDTO;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengefeed.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.imageData.model.entity.ImageData;
import com.knj.mirou.boundedContext.imageData.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
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

        return "view/challenge/createForm";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String create(@Valid ChallengeCreateDTO createDTO, MultipartFile img,
                         BindingResult bindingResult) throws IOException {

        if(bindingResult.hasErrors()) {
            log.error("[ERROR] : " + bindingResult.getAllErrors());
            return "redirect:/challenge/create";
        }

        RsData<Challenge> createRsData = challengeService.tryCreate(createDTO, img);
        if (createRsData.isFail()) {
            createRsData.printResult();
            return "redirect:/challenge/create";
        }

        Challenge createdChallenge = createRsData.getData();
        imageDataService.create(createdChallenge.getId(), ImageTarget.CHALLENGE_IMG, createdChallenge.getImgUrl());

        return "redirect:/challenge/detail/" + createdChallenge.getId();        //굳이 디테일로 리다이렉트?
    }

    @GetMapping("/list")
    public String openedChallengeList(Model model) {

        List<Challenge> openedChallenges = challengeService.getByStatus(ChallengeStatus.OPEN);
        model.addAttribute("openedChallenges", openedChallenges);
        return "view/challenge/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable(value = "id") long challengeId, Principal principal, Model model) {

        Member loginedMember = memberService.getByLoginId(principal.getName()).get();
        Challenge challenge = challengeService.getById(challengeId);
        List<ChallengeFeed> feedList = challengeFeedService.getByChallenge(challenge);
        ImageData challengeImg = imageDataService.getByIdAndTarget(challenge.getId(), ImageTarget.CHALLENGE_IMG);
        Optional<ChallengeMember> OChallengeMember =
                challengeMemberService.getByChallengeAndMember(challenge, loginedMember);

        int memberCount = challengeMemberService.getCountByLinkedChallenge(challenge);

        //TODO: 구조 개선
        if (OChallengeMember.isPresent()) {
            ChallengeMember challengeMember = OChallengeMember.get();
            model.addAttribute("rewardList", challengeMember.getPrivateReward());
            model.addAttribute("isJoin", true);
        } else {
            model.addAttribute("rewardList", challenge.getPublicReward());
            model.addAttribute("isJoin", false);
        }

        //멤버가 챌린지에 3회 이하로 참여했으면 true, 3회 초과로 참여했으면 false
        model.addAttribute("canJoin", challengeMemberService.canJoin(loginedMember));

        if (challengeImg != null) {
            model.addAttribute("challengeImg",
                    imageDataService.getOptimizingUrl(challengeImg.getImageUrl(), OptimizerOption.CHALLENGE_DETAIL));
        } else {
            //FIXME: 없는 이미지 처리
            model.addAttribute("challengeImg",
                    imageDataService.getOptimizingUrl(
                            "https://kr.object.ncloudstorage.com/mirou/etc/no_img.png",
                            OptimizerOption.CHALLENGE_DETAIL));
        }

        model.addAttribute("canWrite", challengeFeedService.alreadyPostedToday(loginedMember, challenge));
        model.addAttribute("challenge", challenge);
        model.addAttribute("feedList", feedList);
        model.addAttribute("memberCount", memberCount);

        return "view/challenge/detail";
    }

}
