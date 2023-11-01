package com.knj.mirou.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {

        return "view/index";
    }

    @GetMapping("/login")
    public String login(){
        return "view/member/login";
    }

    @GetMapping("/myPage")
    public String myPage(){
        return "view/member/mypage";
    }

}
