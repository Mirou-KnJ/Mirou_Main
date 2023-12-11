package com.knj.mirou.boundedContext.reportHistory.controller;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.reportHistory.service.ReportHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportHistoryController {

    private final ReportHistoryService reportHistoryService;

    @PostMapping("/tryReport")
    public ResponseEntity<RsData<Long>> tryReport(@RequestParam Map<String, Object> params) {

        long targetFeedId = Long.parseLong(params.get("targetFeedId").toString());
        long reporterId = Long.parseLong(params.get("reporterId").toString());
        String contents = params.get("contents").toString();

        RsData<Long> reportRs = reportHistoryService.tryReport(targetFeedId, reporterId, contents);

        return ResponseEntity.ok(reportRs);
    }

}
