package com.knj.mirou.boundedContext.challengefeed.controller;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
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

    private final ChallengeFeedService challengeFeedService;

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

        RsData<String> writeRsData = challengeFeedService.writeFeed(challengeId, principal.getName(), img, contents);

        if(writeRsData.isFail()){
            writeRsData.printResult();
            return "redirect:/feed/write/" + challengeId;
        } else {
            writeRsData.printResult();
        }

        return "redirect:/";
    }

}
