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
    public String createChallenge(String name, String contents, LocalDate joinDeadLine, int joinCost, String period, String tag, String method, int level, int requiredNum, String precautions) {

        challengeService.createChallenge(name, contents, period);

        System.out.println("joinDeadLine = " + joinDeadLine);
        System.out.println("tag = " + tag);
        System.out.println("period = " + period);
        System.out.println("method = " + method);
        System.out.println("name = " + name);



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
