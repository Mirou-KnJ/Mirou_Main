package com.knj.mirou.boundedContext.reportHistory.controller;

import com.knj.mirou.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportHistoryController {


    @PostMapping("/tryReport")
    public ResponseEntity<RsData<String>> tryReport(@RequestParam Map<String, Object> params) {

        long reporterId = Long.parseLong(params.get("reporterId").toString());
        long targetFeedId = Long.parseLong(params.get("targetFeedId").toString());
        String contents = params.get("contents").toString();

        System.out.println("contents = " + contents);
        System.out.println("targetFeedId = " + targetFeedId);
        System.out.println("reporterId = " + reporterId);

        return ResponseEntity.ok(RsData.of("S-1", "SUCCESS CALL"));
    }

}
