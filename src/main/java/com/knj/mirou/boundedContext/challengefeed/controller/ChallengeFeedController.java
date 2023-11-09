package com.knj.mirou.boundedContext.challengefeed.controller;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/feed")
public class ChallengeFeedController {

    private final MemberService memberService;
    private final ChallengeService challengeService;
    private final ChallengeFeedService challengeFeedService;
    private final ChallengeMemberService challengeMemberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write/{id}")
    public String writeForm(@PathVariable(value = "id") long challengeId, Model model) {

        model.addAttribute("challengeId", challengeId);

        return "/view/challengeFeed/writeForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write/{id}")
    public String write(@PathVariable(value = "id") long challengeId, MultipartFile img,
                        String contents, Principal principal) throws IOException {

        Member loginedMember = memberService.getByLoginId(principal.getName()).get();
        Challenge challenge = challengeService.getById(challengeId);

        RsData<String> writeRsData = challengeFeedService.writeFeed(challenge, loginedMember, img, contents);

        if(writeRsData.isFail()){
            writeRsData.printResult();
            return "redirect:/feed/write/" + challengeId;
        } else {
            challengeMemberService.updateSuccess(loginedMember, challenge);
            writeRsData.printResult();
        }

        return "redirect:/";
    }

}
