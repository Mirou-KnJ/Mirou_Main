package com.knj.mirou.boundedContext.challenge.controller;

import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping("/createTest")
    public String testCreate() {

        challengeService.createTest();

        return "redirect:/";
    }

}
