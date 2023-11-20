package com.knj.mirou.base.controller;

import com.knj.mirou.boundedContext.challenge.config.LabelConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final LabelConfig labelConfig;

    @GetMapping("/")
    public String home() {

        return "view/index";
    }

}
