package com.knj.mirou.base.controller;

import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeLabel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home() {

        System.out.println("워터 : " + ChallengeLabel.WATER.getLabels());
        System.out.println("독서 : " + ChallengeLabel.BOOKS.getLabels());

        return "view/index";
    }

}
