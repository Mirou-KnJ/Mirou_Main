package com.knj.mirou.boundedContext.reward.controller;

import com.knj.mirou.boundedContext.reward.service.PublicRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequiredArgsConstructor
@RequestMapping("/reward")
public class RewardController {

    private final PublicRewardService publicRewardService;

    @GetMapping("/setting/{challengeId}")
    public String settingForm(@PathVariable(value = "challengeId") long challengeId) {


        return "/view/reward/settingForm" +challengeId;
    }



}
