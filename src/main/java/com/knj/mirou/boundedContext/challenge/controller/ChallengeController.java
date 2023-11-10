package com.knj.mirou.boundedContext.challenge.controller;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengefeed.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/create")
    public String createForm() {

        return "/view/challenge/createForm";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String createChallenge(String name, String contents, int joinCost, LocalDate joinDeadLine, int period,
                                  String tag, String method, int level, String precaution) {

        challengeService.create(name, contents, joinCost, joinDeadLine, period, tag, method, level, precaution);

        return "redirect:/";
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

        //FIXME: challengeMemberService 여기서 호출 X? => 순환 참조 문제 어떻게 할 것인가.
        if(challengeMemberService.getByChallengeAndMember(challenge, loginedMember).isPresent()) {
            model.addAttribute("isJoin", true);
        } else {
            model.addAttribute("isJoin", false);
        }

        model.addAttribute("challenge", challenge);
        model.addAttribute("feedList", feedList);

        return "view/challenge/detail";
    }

}
