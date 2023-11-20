package com.knj.mirou.boundedContext.challenge.controller;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeCreateDTO;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeDetailDTO;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
import com.knj.mirou.boundedContext.imageData.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final ChallengeFeedService challengeFeedService;
    private final ImageDataService imageDataService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/create")
    public String createForm() {

        return "view/challenge/createForm";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String create(@Valid ChallengeCreateDTO createDTO, MultipartFile img,
                         BindingResult bindingResult) throws IOException {

        if(bindingResult.hasErrors()) {
            log.error("[ERROR] : " + bindingResult.getAllErrors());
            return "redirect:/challenge/create";
        }

        RsData<String> tryUploadRs = imageDataService.tryUploadImg(img, ImageTarget.CHALLENGE_IMG);
        if(tryUploadRs.isFail()) {
            tryUploadRs.printResult();
            return "redirect:/challenge/create";
        }

        RsData<Challenge> createRsData = challengeService.tryCreate(createDTO, tryUploadRs.getData());
        if (createRsData.isFail()) {
            createRsData.printResult();
            return "redirect:/challenge/create";
        }

        Challenge createdChallenge = createRsData.getData();
        imageDataService.create(createdChallenge.getId(), ImageTarget.CHALLENGE_IMG, createdChallenge.getImgUrl());

        return "redirect:/reward/setting/" + createdChallenge.getId();
    }

    @GetMapping("/list")
    public String openedChallengeList(Model model) {

        List<Challenge> openedChallenges = challengeService.getByStatus(ChallengeStatus.OPEN);
        model.addAttribute("openedChallenges", openedChallenges);
        return "view/challenge/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable(value = "id") long challengeId, Principal principal, Model model) {

        String loginId = principal.getName();

        RsData<ChallengeDetailDTO> getDetailRs = challengeService.getDetailDTO(challengeId, loginId);
        if(getDetailRs.isFail()) {
            getDetailRs.printResult();
            return "redirect:/";        //fixme
        }

        ChallengeDetailDTO detailDTO = getDetailRs.getData();
        Challenge challenge = detailDTO.getChallenge();
        detailDTO.setCanWrite(challengeFeedService.alreadyPostedToday(detailDTO.getLoginMember(), challenge));
        detailDTO.setRecently3Feeds(challengeFeedService.getRecently3Feed(challenge));

        model.addAttribute("detailDTO", detailDTO);
        model.addAttribute("challengeImg",
                imageDataService.getOptimizingUrl(challenge.getImgUrl(), OptimizerOption.CHALLENGE_DETAIL));

        return "view/challenge/detail";
    }

}
