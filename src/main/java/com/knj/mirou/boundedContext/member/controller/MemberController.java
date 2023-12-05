package com.knj.mirou.boundedContext.member.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengemember.model.enums.Progress;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.inventory.model.entity.Inventory;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final Rq rq;

    private final MemberService memberService;
    private final ChallengeService challengeService;
    private final ChallengeMemberService challengeMemberService;

    @GetMapping("/login")
    public String showLoginPage(){
        return "view/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String showMyPage(Model model){

        Member member = rq.getMember();
        int inProgressNum = challengeMemberService
                .getCountByLinkedMemberAndProgress(member, Progress.IN_PROGRESS);
        int endProgressNum = challengeMemberService
                .getCountByLinkedMemberAndProgress(member, Progress.PROGRESS_END);

        model.addAttribute("member", member);
        model.addAttribute("inProgressNum", inProgressNum);
        model.addAttribute("endProgressNum", endProgressNum);

        return "view/member/myPage";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String adminPage(Model model) {

        List<Challenge> challengeList = challengeService.getAllList();
        model.addAttribute("challengeList", challengeList);

        return "view/admin/adminPage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/inventory")
    public String showInventory(Model model) {

        Member member = rq.getMember();

        List<Inventory> inventories = member.getInventory();

        model.addAttribute("inventories", inventories);

        return "view/member/inventory";
    }
}
