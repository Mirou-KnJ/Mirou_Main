package com.knj.mirou.boundedContext.challengemember.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/challengeMember")
public class ChallengeMemberController {

    private final Rq rq;

    private final ChallengeService challengeService;
    private final ChallengeMemberService challengeMemberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/join/{id}")
    public String join(@PathVariable(value = "id") long challengeId) {

        Optional<Challenge> OChallenge = challengeService.getById(challengeId);
        if (OChallenge.isEmpty()) {
            return rq.historyBack("챌린지 정보가 유효하지 않습니다.");
        }

        RsData<Long> joinRs = challengeMemberService.join(OChallenge.get(), rq.getMember());
        if (joinRs.isFail()) {
            return rq.historyBack(joinRs);
        }

        return rq.redirectWithMsg("/challenge/detail/" + challengeId, joinRs);
    }
}
