package com.knj.mirou.boundedContext.challenge.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeCreateDTO;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeDetailDTO;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeLabel;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.imageData.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {

    private final Rq rq;

    private final ChallengeService challengeService;
    private final ImageDataService imageDataService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute("categories", challengeService.getAllCategories());
        model.addAttribute("methods", challengeService.getAllMethods());

        return "view/challenge/createForm";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String create(@Valid ChallengeCreateDTO createDTO, BindingResult bindingResult,
                         MultipartFile img) throws IOException {

        if(bindingResult.hasErrors()) {
            return rq.historyBack(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        RsData<String> uploadImgRs = imageDataService.tryUploadImg(img, ImageTarget.CHALLENGE_IMG);
        if (uploadImgRs.isFail()) {
            uploadImgRs.printResult();
            return rq.historyBack(uploadImgRs);
        }

        String imgUrl = uploadImgRs.getData();

        RsData<Long> createRs = challengeService.create(createDTO, imgUrl);
        if (createRs.isFail()) {
            createRs.printResult();
            return rq.historyBack(createRs);
        }

        long challengeId = createRs.getData();

        imageDataService.create(challengeId, ImageTarget.CHALLENGE_IMG, imgUrl);

        return rq.redirectWithMsg("/reward/setting/" + challengeId, createRs);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list/{tag}")
    public String openedChallengeList(@PathVariable(required = false) String tag, Model model) {

        Member member = rq.getMember();

        List<ChallengeMember> myChallengeInfos = challengeService.getMyChallengeInfos(member);
        List<Challenge> myChallenges = challengeService.getChallengesByInfos(myChallengeInfos);
        List<Challenge> notMineChallenges = challengeService.getNotMineChallenges(member, myChallenges, tag);

        Map<Long, String> challengeImages = imageDataService.getListImages(notMineChallenges);
        Map<Long, String> myImages = imageDataService.getListImages(myChallenges);
        challengeImages.putAll(myImages);

        model.addAttribute("member", member);
        model.addAttribute("myChallengeInfos", myChallengeInfos);
        model.addAttribute("openedChallenges", notMineChallenges);
        model.addAttribute("challengeImages", challengeImages);

        return "view/challenge/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable(value = "id") long challengeId, Model model) {

        Member member = rq.getMember();

        RsData<ChallengeDetailDTO> getDetailRs = challengeService.getDetailDTO(challengeId, member);
        if (getDetailRs.isFail()) {
            getDetailRs.printResult();
            return rq.historyBack(getDetailRs);
        }

        ChallengeDetailDTO detailDTO = getDetailRs.getData();

        model.addAttribute("detailDTO", detailDTO);
        model.addAttribute("challengeImg", detailDTO.getDetailImg());

        return "view/challenge/detail";
    }

    @ResponseBody
    @PostMapping("/getLabels")
    public ResponseEntity<Map<String, Object>> postLabelList(@RequestParam Map<String, Object> params) {

        Object selectedValue = params.get("selectedValue");
        String selectedLabelType = selectedValue.toString();

        List<String> labels = ChallengeLabel.valueOf(selectedLabelType).getLabels();

        Map<String, Object> result = new HashMap<>();

        result.put("labels", labels);

        return ResponseEntity.ok(result);
    }

}
