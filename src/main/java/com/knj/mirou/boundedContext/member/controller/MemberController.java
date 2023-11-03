package com.knj.mirou.boundedContext.member.controller;

import com.knj.mirou.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String showLoginPage(){
        return "/view/member/login";
    }

    @GetMapping("/myPage")
    public String showMyPage(){
        return "/view/member/mypage";
    }

}
