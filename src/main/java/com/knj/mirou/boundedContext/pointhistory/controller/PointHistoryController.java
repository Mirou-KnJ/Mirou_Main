package com.knj.mirou.boundedContext.pointhistory.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.point.entity.Point;
import com.knj.mirou.boundedContext.pointhistory.entity.PointHistory;
import com.knj.mirou.boundedContext.pointhistory.service.PointHistoryService;
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
@RequestMapping("/point")
public class PointHistoryController {

    private final Rq rq;
    private final PointHistoryService pointHistoryService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/history")
    public String showPointHistory(Model model) {

        Member loginedMember = rq.getMember();
        Point point = loginedMember.getPoint();
        List<PointHistory> pointHistories = pointHistoryService.getAllOrderedDesc(loginedMember);

        model.addAttribute("memberPoint", point);
        model.addAttribute("pointHistories", pointHistories);

        return "view/point/history";
    }
}
