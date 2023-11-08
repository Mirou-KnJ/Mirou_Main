package com.knj.mirou.boundedContext.challenge.controller;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.coin.entity.Coin;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Date;

@Controller
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping("/createChallenge")
    public String createChallenge(){

        return "view/challenge/createChallenge";
    }

    @PostMapping("/createChallenge")
    public String createChallenge(String name, String contents, int joinCost, LocalDate joinDeadLine, String period, String tag, String method, int level, String status, String precaution) {

        challengeService.createChallenge(name, contents, joinCost, joinDeadLine, period, tag, method, level, status, precaution);

        System.out.println("name = " + name);
        System.out.println("contents = " + contents);
        System.out.println("joinCost = " + joinCost);
        System.out.println("joinDeadLine = " + joinDeadLine);
        System.out.println("period = " + period);
        System.out.println("tag = " + tag);
        System.out.println("method = " + method);
        System.out.println("level = " + level);
        System.out.println("status = " + status);
        System.out.println("precaution = " + precaution);


        return "redirect:/";
    }
    @GetMapping("/allChallengeList")
    public String allChallengeList(Model model){
        model.addAttribute("challenges", challengeService.getAllChallenges());
        return "view/challenge/allChallengeList";
    }

    @GetMapping("/detailChallenge")
    public String detailChallenge(){
        return "view/challenge/detailChallenge";
    }

}
