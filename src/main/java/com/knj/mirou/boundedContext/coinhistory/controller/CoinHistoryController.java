package com.knj.mirou.boundedContext.coinhistory.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.boundedContext.coinhistory.entity.CoinHistory;
import com.knj.mirou.boundedContext.coinhistory.service.CoinHistoryService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/coin")
public class CoinHistoryController {

    private final Rq rq;
    private final CoinHistoryService coinHistoryService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/history")
    public String showCoinHistory(Model model) {

        Member loginedMember = rq.getMember();

        List<CoinHistory> coinHistories = coinHistoryService.getAllOrderedDesc(loginedMember);

        model.addAttribute("memberCoin", loginedMember.getCoin());
        model.addAttribute("coinHistories", coinHistories);

        return "view/coin/history";
    }



}
