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
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping("/create")
    public String createChallenge(){

        return "view/challenge/createChallenge";
    }

    @PostMapping("/create")
    public String createChallenge(String name, String contents, int joinCost, LocalDate joinDeadLine,
                                  String period, String tag, String method, int level, String status, String precaution) {

        challengeService.create(name, contents, joinCost, joinDeadLine,
                                period, tag, method, level, status, precaution);

        return "redirect:/";
    }
    @GetMapping("/allChallengeList")
    public String allChallengeList(Model model){

        List<Challenge> ChallengeList = challengeService.allChallengeList();
        model.addAttribute("allChallengeList", challengeService.allChallengeList());
        return "view/challenge/allChallengeList";
    }

//    @GetMapping("/detail/{challengeId}")
//    public String detailChallenge(){
//        return "view/challenge/detailChallenge";
//    }

}
