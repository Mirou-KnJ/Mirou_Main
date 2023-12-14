package com.knj.mirou.boundedContext.reward.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.base.rsData.RsData;
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

import java.util.Optional;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/reward")
public class RewardController {

    private final Rq rq;
    private final PublicRewardService publicRewardService;
    private final ChallengeService challengeService;

    @GetMapping("/setting/{challengeId}")
    public String settingForm(@PathVariable(value = "challengeId") long challengeId, Model model) {

        Optional<Challenge> OChallenge = challengeService.getById(challengeId);
        if (OChallenge.isEmpty()) {
            log.error("세팅 대상 챌린지를 찾을 수 없습니다.");
            return "redirect:/";        //FIXME
        }

        Challenge challenge = OChallenge.get();
        model.addAttribute("challenge", challenge);

        return "view/reward/settingForm";
    }

    @PostMapping("/setting")
    public String createReward(long id, int round, String rewardType, String reward) {

        RsData<Long> createRs = publicRewardService.create(id, round, rewardType, reward);
        if (createRs.isFail()) {
            return rq.historyBack(createRs);
        }

        return rq.redirectWithMsg("/reward/setting/" + id, createRs);
    }


    @GetMapping("/confirmSettings/{id}")
    public String getConfirmForm(@PathVariable(value = "id") long challengeId, Model model) {

        Optional<Challenge> OChallenge = challengeService.getById(challengeId);
        if (OChallenge.isEmpty()) {
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
        if (OChallenge.isEmpty()) {
            return rq.historyBack("세팅 대상 챌린지를 찾을 수 없습니다.");
        }

        challengeService.opening(OChallenge.get());

        return rq.redirectWithMsg("/member/admin", "세팅이 완료되었습니다.");
    }

    @PostMapping("/deleteReward/{rewardId}")
    public String deleteReward(@PathVariable(value = "rewardId") long rewardId) {

        PublicReward publicReward = publicRewardService.getById(rewardId);
        Long challengeId = publicReward.getLinkedChallenge().getId();
        publicRewardService.deleteReward(rewardId);
        return "redirect:/reward/setting/" + challengeId;
    }
}
