package com.knj.mirou.boundedContext.reward.controller;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.reward.model.entity.PrivateReward;
import com.knj.mirou.boundedContext.reward.model.entity.PublicReward;
import com.knj.mirou.boundedContext.reward.service.PublicRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/reward")
public class RewardController {

    private final PublicRewardService publicRewardService;
    private final ChallengeService challengeService;

    @GetMapping("/setting/{challengeId}")
    public String settingForm(@PathVariable(value = "challengeId") long challengeId, Model model) {

        Challenge byId = challengeService.getById(challengeId);
        List<PublicReward> rewardList = byId.getPublicReward();

        model.addAttribute("challengeId", challengeId);
        model.addAttribute("rewardList", rewardList);

        return "view/reward/settingForm";
    }

    @PostMapping("/setting")
    public String setPublicReward(long id, int round, String rewardType, String reward) {

        publicRewardService.create(id, round, rewardType, reward);

        return "redirect:/reward/setting/" + id;
    }

    @GetMapping("/confirmSettings/{id}")
    public String getConfirmForm(@PathVariable(value = "id") long challengeId, Model model) {

        Challenge challenge = challengeService.getById(challengeId);
        model.addAttribute("challenge", challenge);

        return "view/reward/confirmForm";
    }

    @PostMapping("/confirmSettings/{id}")
    public String confirmSettings(@PathVariable(value = "id") long challengeId) {

        //TODO: 유효성 검사 (진행 일수에 적절한 보상 설정인지 등)
        Challenge challenge = challengeService.getById(challengeId);
        challengeService.opening(challenge);

        return "redirect:/member/admin";
    }
}
