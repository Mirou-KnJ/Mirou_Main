package com.knj.mirou.boundedContext.challengefeed.controller;

import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
public class ChallengeFeedRestController {

    private final ChallengeFeedService challengeFeedService;

    @PostMapping("/write")
    public String writeRequest(MultipartFile img) throws IOException {

        String resultUrl = challengeFeedService.uploadImg(img);

        return resultUrl;
    }


}
