package com.knj.mirou.boundedContext.challenge.controller;

import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final MemberService memberService;
    private final ChallengeMemberService challengeMemberService;

    @GetMapping("/create")
    public String createChallenge() {

        return "view/challenge/createChallenge";
    }

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
    public String detailChallenge(@PathVariable(value = "id") long challengeId, Principal principal, Model model) {

        Member loginedMember = memberService.getByLoginId(principal.getName()).get();

        //FIXME : 원래 ChallengeMember 서비스는 여기서 호출하면 안되며, 서비스 처리도 컨트롤러에서 이루어지면 안좋음.
        Optional<ChallengeMember> byMember = challengeMemberService.getByMember(loginedMember);

        if(byMember.isPresent()) {
            model.addAttribute("isJoin", true);
        } else {
            model.addAttribute("isJoin", false);
        }

        Challenge challenge = challengeService.getById(challengeId);

        model.addAttribute("challenge", challenge);

        return "view/challenge/detail";
    }

}
