package com.knj.mirou.boundedContext.challengefeed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
public class ChallengeFeedRestController {

    @PostMapping("/write")
    public void writeRequest(MultipartFile img) {

        System.out.println(img.getName());
        System.out.println("img = " + img.getOriginalFilename());

    }


}
