package com.knj.mirou.boundedContext.reward.controller;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.reward.model.entity.PublicReward;
import com.knj.mirou.boundedContext.reward.service.PublicRewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/reward")
public class RewardController {

    private final PublicRewardService publicRewardService;
    private final ChallengeService challengeService;

    @GetMapping("/setting/{challengeId}")
    public String settingForm(@PathVariable(value = "challengeId") long challengeId, Model model) {

        Optional<Challenge> OChallenge = challengeService.getById(challengeId);
        if(OChallenge.isEmpty()) {
            log.error("세팅 대상 챌린지를 찾을 수 없습니다.");
            return "redirect:/";        //FIXME
        }

        Challenge challenge = OChallenge.get();
        model.addAttribute("challenge", challenge);

        return "view/reward/settingForm";
    }

    @PostMapping("/setting")
    public String setPublicReward(long id, int round, String rewardType, String reward) {

        publicRewardService.create(id, round, rewardType, reward);

        return "redirect:/reward/setting/" + id;
    }

    @GetMapping("/confirmSettings/{id}")
    public String getConfirmForm(@PathVariable(value = "id") long challengeId, Model model) {

        Optional<Challenge> OChallenge = challengeService.getById(challengeId);
        if(OChallenge.isEmpty()) {
            log.error("세팅 대상 챌린지를 찾을 수 없습니다");
            return "redirect:/";        //FIXME
        }

        model.addAttribute("challenge", OChallenge.get());

        return "view/reward/confirmForm";
    }

    @PostMapping("/confirmSettings/{id}")
    public String confirmSettings(@PathVariable(value = "id") long challengeId) {

        //TODO: 유효성 검사 (진행 일수에 적절한 보상 설정인지 등)
        Optional<Challenge> OChallenge = challengeService.getById(challengeId);
        if(OChallenge.isEmpty()) {
            log.info("세팅 대상 챌린지를 찾을 수 없습니다.");
            return "redirect:/confirmSettings/" + challengeId;
        }

        challengeService.opening(OChallenge.get());

        return "redirect:/member/admin";
    }
}
