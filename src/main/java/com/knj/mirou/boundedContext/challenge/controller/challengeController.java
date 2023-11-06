package com.knj.mirou.boundedContext.challenge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class challengeController {

    @GetMapping("challenge/createChallenge")
    public String createChallenge(){
        return "view/challenge/createChallenge";
    }

    @GetMapping("challenge/detailChallenge")
    public String detailChallenge(){
        return "view/challenge/detailChallenge";
    }
}
