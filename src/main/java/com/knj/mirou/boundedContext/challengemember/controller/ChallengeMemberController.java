package com.knj.mirou.boundedContext.challengemember.controller;

import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/challengeMember")
public class ChallengeMemberController {

    private final ChallengeMemberService challengeMemberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/join/{id}")
    public String join(@PathVariable(value = "id") long challengeId, Principal principal) {

        challengeMemberService.join(challengeId, principal.getName());

        return "redirect:/challenge/detail/" + challengeId;
    }

}
