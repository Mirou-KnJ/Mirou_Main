package com.knj.mirou.boundedContext.challenge.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.config.MapConfigProperties;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeCreateDTO;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeDetailDTO;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeLabel;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeTag;
import com.knj.mirou.boundedContext.challenge.model.enums.MapCategory;
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
    private final MapConfigProperties mapConfigProps;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/create")
    public String createForm() {

        return "view/challenge/createForm";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String create(@Valid ChallengeCreateDTO createDTO, BindingResult bindingResult,
                         MultipartFile img) throws IOException {

        if(bindingResult.hasErrors()) {
            log.error("[ERROR] : " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return rq.historyBack(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        RsData<String> tryUploadRs = imageDataService.tryUploadImg(img, ImageTarget.CHALLENGE_IMG);
        if (tryUploadRs.isFail()) {
            tryUploadRs.printResult();
            return rq.historyBack(tryUploadRs);
        }

        RsData<Challenge> createRsData = challengeService.tryCreate(createDTO, tryUploadRs.getData());
        if (createRsData.isFail()) {
            createRsData.printResult();
            return rq.historyBack(createRsData);
        }

        Challenge createdChallenge = createRsData.getData();
        imageDataService.create(createdChallenge.getId(), ImageTarget.CHALLENGE_IMG, createdChallenge.getImgUrl());

        return "redirect:/reward/setting/" + createdChallenge.getId();
    }


    @PreAuthorize("isAuthenticated()")      //FIXME: principal null 오류 임시 방지, 수정 필요
    @GetMapping("/list")
    public String openedChallengeList(Model model, Principal principal) {

        List<Challenge> openedChallenges = challengeService.getByStatus(ChallengeStatus.OPEN);
        openedChallenges.sort(Comparator.comparing(Challenge::getCreateDate).reversed());
        List<Challenge> myValidChallengeList = challengeService.getMyValidChallengeList(principal.getName());
        String loginId = principal.getName();
        Optional<Member> ObyLoginId = memberService.getByLoginId(loginId);

        if(ObyLoginId.isPresent()) {
            Member member = ObyLoginId.get();
            model.addAttribute("member", member);
        } else {
            return null;
        }

        model.addAttribute("openedAndValid",
                challengeService.getNotMineOpenedChallenge(myValidChallengeList, openedChallenges));
        model.addAttribute("myValidChallengeList", myValidChallengeList);
        model.addAttribute("ListOption", OptimizerOption.CHALLENGE_LIST);
        model.addAttribute("ImageDateService", imageDataService);
        model.addAttribute("openedChallenges", openedChallenges);

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list/{tag}")
    public String filterChallenges(@PathVariable(value = "tag") String tag, Model model, Principal principal){

        List<Challenge> myValidChallengeList = challengeService.getMyValidChallengeList(principal.getName());
        List<Challenge> openedChallenges = challengeService.getOpenedChallengeByTag(ChallengeTag.valueOf(tag));
        openedChallenges.sort(Comparator.comparing(Challenge::getCreateDate).reversed());
        Member member = rq.getMember();

        model.addAttribute("openedAndValid",
                challengeService.getNotMineOpenedChallenge(myValidChallengeList, openedChallenges));
        model.addAttribute("member", member);
        model.addAttribute("myValidChallengeList", myValidChallengeList);
        model.addAttribute("openedChallenges", openedChallenges);
        model.addAttribute("ListOption", OptimizerOption.CHALLENGE_LIST);
        model.addAttribute("ImageDateService", imageDataService);

        return "view/challenge/list";
    }

    @GetMapping("/map/test")
    public String mapTestPage(Model model) {

        String mapKey = mapConfigProps.getKey();

        //FIXME: 챌린지에 설정된 카테고리로 반환되어야 함.
        model.addAttribute("category", MapCategory.ATTRACTION);

        model.addAttribute("mapKey", mapKey);


        return "view/challenge/mapTest";
    }

}
