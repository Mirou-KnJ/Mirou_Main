package com.knj.mirou.boundedContext.challenge.controller;

import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;

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
        model.addAttribute("challenges", challengeService.getAllList());
        return "view/challenge/allChallengeList";
    }

    @GetMapping("/detail")
    public String detailChallenge() {
        return "view/challenge/detailChallenge";
    }

}
