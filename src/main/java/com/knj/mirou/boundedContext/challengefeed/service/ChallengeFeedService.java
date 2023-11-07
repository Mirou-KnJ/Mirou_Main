package com.knj.mirou.boundedContext.challengefeed.service;

import com.knj.mirou.boundedContext.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeFeedService {

    private final ImageService imageService;

    public String uploadImg(MultipartFile img) throws IOException {

        return imageService.uploadImg(img);
    }

}
