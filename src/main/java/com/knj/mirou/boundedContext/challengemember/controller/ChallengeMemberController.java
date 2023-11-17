package com.knj.mirou.boundedContext.challengemember.controller;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.reward.service.PrivateRewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/challengeMember")
public class ChallengeMemberController {

    private final ChallengeMemberService challengeMemberService;
    private final ChallengeService challengeService;
    private final PrivateRewardService privateRewardService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/join/{id}")
    public String join(@PathVariable(value = "id") long challengeId, Principal principal) {

        Optional<Challenge> OChallenge = challengeService.getById(challengeId);
        if(OChallenge.isEmpty()) {
            log.error("참여하려는 챌린지를 찾을 수 없습니다");
            return "redirect:/challenge/detail" + challengeId;
        }

        Challenge linkedChallenge = OChallenge.get();

        ChallengeMember challengeMember = challengeMemberService.join(linkedChallenge, principal.getName());

        privateRewardService.create(linkedChallenge, challengeMember);

        return "redirect:/challenge/detail/" + challengeId;
    }


}
