package com.knj.mirou.boundedContext.challenge.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeCreateDTO;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeDetailDTO;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeLabel;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeTag;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengefeed.model.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
import com.knj.mirou.boundedContext.imageData.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {

    private final Rq rq;
    private final ChallengeService challengeService;
    private final ChallengeFeedService challengeFeedService;
    private final ImageDataService imageDataService;
    private final MemberService memberService;

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

        return "redirect:/reward/setting/" + challengeId;
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list/{tag}")
    public String openedChallengeList(@PathVariable(required = false) String tag, Model model) {

        Member member = rq.getMember();

        List<Challenge> myInProgressChallenges = challengeService.getMyChallenges(member);

        List<Challenge> notMineChallenges = challengeService.getNotMineChallenges(member, myInProgressChallenges, tag);

        Map<Long, String> challengeImages = imageDataService.getListImages(notMineChallenges);
        Map<Long, String> myImages = imageDataService.getListImages(myInProgressChallenges);
        challengeImages.putAll(myImages);

        model.addAttribute("member", member);
        model.addAttribute("myChallenges", myInProgressChallenges);
        model.addAttribute("openedChallenges", notMineChallenges);
        model.addAttribute("challengeImages", challengeImages);

        return "view/challenge/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable(value = "id") long challengeId, Principal principal, Model model) {

        String loginId = principal.getName();

        RsData<ChallengeDetailDTO> getDetailRs = challengeService.getDetailDTO(challengeId, loginId);
        if (getDetailRs.isFail()) {
            getDetailRs.printResult();
            return rq.historyBack(getDetailRs);
        }

        ChallengeDetailDTO detailDTO = getDetailRs.getData();
        Challenge challenge = detailDTO.getChallenge();
        detailDTO.setCanWrite(challengeFeedService.alreadyPostedToday(detailDTO.getLoginMember(), challenge));

        List<ChallengeFeed> recently3Feed = challengeFeedService.getRecently3Feed(challenge);
        detailDTO.setRecently3Feeds(recently3Feed);

        //FIXME: 임시코드. 리팩토링 필수
        List<String> imgList = new ArrayList<>();
        for (ChallengeFeed feed : recently3Feed) {
            imgList.add(imageDataService.getOptimizingUrl(feed.getImgUrl(), OptimizerOption.FEED_MODAL));
        }

        detailDTO.setFeedOptimizedImages(imgList);
        model.addAttribute("detailDTO", detailDTO);
        model.addAttribute("challengeImg",
                imageDataService.getOptimizingUrl(challenge.getImgUrl(), OptimizerOption.CHALLENGE_DETAIL));

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
