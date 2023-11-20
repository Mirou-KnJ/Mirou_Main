package com.knj.mirou.boundedContext.challengemember.controller;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.challengemember.model.enums.Progress;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.reward.service.PrivateRewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.ChallengeState;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Member;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/challengeMember")
public class ChallengeMemberController {

    private final ChallengeService challengeService;
    private final ChallengeMemberService challengeMemberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/join/{id}")
    public String join(@PathVariable(value = "id") long challengeId, Principal principal) {

        Optional<Challenge> OChallenge = challengeService.getById(challengeId);
        if(OChallenge.isEmpty()) {
            log.error("챌린지 정보가 유효하지 않습니다.");
            return "redirect:/";        //FIXME
        }

        RsData<String> joinRs = challengeMemberService.join(OChallenge.get(), principal.getName());

        if(joinRs.isFail()) {
            joinRs.printResult();
            return "redirect:/challenge/detail" + challengeId;
        }

        return "redirect:/challenge/detail/" + challengeId;
    }

//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/list")
//    public String progressChallengeList(Model model, Principal principal){
//
//
//        return "view/challenge/list";
//    }


}
