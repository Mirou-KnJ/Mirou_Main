package com.knj.mirou.boundedContext.member.controller;

import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String showLoginPage(){
        return "/view/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String showMyPage(Principal principal, Model model){

        String loginId = principal.getName();

        Optional<Member> ObyLoginId = memberService.getByLoginId(loginId);

        if(ObyLoginId.isPresent()) {
            model.addAttribute("member", ObyLoginId.get());
        } else {
            //FIXME 에러페이지 렌더링?
            return null;
        }

        return "/view/member/mypage";
    }

}
