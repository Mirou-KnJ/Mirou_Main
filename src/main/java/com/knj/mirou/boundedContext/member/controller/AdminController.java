package com.knj.mirou.boundedContext.member.controller;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.member.model.dtos.ChallengeReportDTO;
import com.knj.mirou.boundedContext.member.model.dtos.CoinReportDTO;
import com.knj.mirou.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final MemberService memberService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/report")
    public String adminReport() {

        return "view/admin/report/index";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/report/challenge")
    public String challengeReport(Model model) {

        ChallengeReportDTO reportDTO = memberService.getChallengeReportDto();

        model.addAttribute("reportDTO", reportDTO);

        return "view/admin/report/challengeReport";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/report/coin")
    public String coinReport(Model model) {

        CoinReportDTO reportDTO = memberService.getCoinReportDto();

        model.addAttribute("reportDTO", reportDTO);

        return "view/admin/report/coinReport";
    }

}
