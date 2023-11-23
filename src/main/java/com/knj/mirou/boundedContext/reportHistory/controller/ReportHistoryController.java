package com.knj.mirou.boundedContext.reportHistory.controller;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.reportHistory.service.ReportHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportHistoryController {

    private final ReportHistoryService reportHistoryService;

    @PostMapping("/tryReport")
    public ResponseEntity<Map<String, Object>> tryReport(@RequestParam Map<String, Object> params) {

        long targetFeedId = Long.parseLong(params.get("targetFeedId").toString());
        long reporterId = Long.parseLong(params.get("reporterId").toString());
        String contents = params.get("contents").toString();

        RsData<String> reportRs = reportHistoryService.tryReport(targetFeedId, reporterId, contents);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("resultCode", reportRs.getResultCode());
        resultMap.put("msg", reportRs.getMsg());

        return ResponseEntity.ok(resultMap);
    }

}
