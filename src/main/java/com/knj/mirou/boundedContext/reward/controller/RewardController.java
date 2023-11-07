package com.knj.mirou.boundedContext.reward.controller;

import com.knj.mirou.boundedContext.reward.model.entity.PublicReward;
import com.knj.mirou.boundedContext.reward.service.PublicRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/reward")
public class RewardController {

    private final PublicRewardService publicRewardService;

    @GetMapping("/setting/{challengeId}")
    public String settingForm(@PathVariable(value = "challengeId") long challengeId, Model model) {

        List<PublicReward> rewardList = publicRewardService.getAllReward();

        model.addAttribute("challengeId", challengeId);
        model.addAttribute("rewardList", rewardList);

        return "/view/reward/settingForm";
    }

    @PostMapping("/setting")
    public String setPublicReward(int id, int round, String rewardType, String reward) {

        StringBuilder sb = new StringBuilder();

        sb.append("id : " + id);
        sb.append("round : " + round);
        sb.append("rewardType : " + rewardType);
        sb.append("reward : " + reward);

        publicRewardService.create(id, round, rewardType, reward);

        return "redirect:/reward/setting/" + id;
    }


}
