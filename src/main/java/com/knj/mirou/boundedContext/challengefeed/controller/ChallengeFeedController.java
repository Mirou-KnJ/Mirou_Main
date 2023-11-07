package com.knj.mirou.boundedContext.challengefeed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/feed")
public class ChallengeFeedController {

    @GetMapping("/writeForm")
    public String writeFeed() {

        return "/view/challengefeed/writeForm";
    }

}
