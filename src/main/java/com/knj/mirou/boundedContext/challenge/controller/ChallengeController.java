package com.knj.mirou.boundedContext.challenge.controller;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/detail/{id}")
    public String detailChallenge(@PathVariable(value = "id") long challengeId, Model model) {

        Challenge challenge = challengeService.getById(challengeId);

        model.addAttribute("challenge", challenge);

        return "view/challenge/detail";
    }

}
