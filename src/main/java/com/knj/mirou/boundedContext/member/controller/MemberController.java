package com.knj.mirou.boundedContext.member.controller;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengemember.model.enums.Progress;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final ChallengeMemberService challengeMemberService;
    private final ChallengeService challengeService;

    @GetMapping("/login")
    public String showLoginPage(){

        return "view/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String showMyPage(Principal principal, Model model){

        String loginId = principal.getName();

        Optional<Member> ObyLoginId = memberService.getByLoginId(loginId);

        if(ObyLoginId.isPresent()) {

            Member member = ObyLoginId.get();
            model.addAttribute("member", member);

            //fixme 컨트롤러에서 다른 위치의 서비스 호출?
            long inProgressCount = challengeMemberService.getCountByMemberAndProgress(member, Progress.IN_PROGRESS);
            long endCount = challengeMemberService.getCountByMemberAndProgress(member, Progress.PROGRESS_END);

            model.addAttribute("inProgressCount", inProgressCount);
            model.addAttribute("endCount", endCount);

        } else {
            //FIXME 에러페이지 렌더링?
            return null;
        }

        return "view/member/mypage";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String adminPage(Model model) {

        List<Challenge> challengeList = challengeService.getAllList();

        model.addAttribute("challengeList", challengeList);

        return "view/admin/adminPage";
    }

}
