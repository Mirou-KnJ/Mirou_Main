package com.knj.mirou.boundedContext.member.controller;

import com.knj.mirou.boundedContext.member.model.dtos.ChallengeReportDTO;
import com.knj.mirou.boundedContext.member.model.dtos.CurrencyReportDTO;
import com.knj.mirou.boundedContext.member.model.dtos.ProductReportDTO;
import com.knj.mirou.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @GetMapping("/report/currency")
    public String currencyReport(Model model) {

        CurrencyReportDTO coinReportDTO = memberService.getCoinReportDto();
        CurrencyReportDTO pointReportDTO = memberService.getPointReportDto();

        model.addAttribute("coinReportDTO", coinReportDTO);
        model.addAttribute("pointReportDTO", pointReportDTO);

        return "view/admin/report/currencyReport";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/report/product")
    public String productReport(Model model) {

        ProductReportDTO productReportDTO = memberService.getProductReportDto();

        model.addAttribute("productReportDTO", productReportDTO);

        return "view/admin/report/productReport";
    }

}
