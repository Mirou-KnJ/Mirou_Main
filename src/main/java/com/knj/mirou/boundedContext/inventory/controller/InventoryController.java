package com.knj.mirou.boundedContext.inventory.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.inventory.service.InventoryService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final Rq rq;

    private final InventoryService inventoryService;

    @GetMapping("/use/{inventoryId}")
    public String usingProduct(@PathVariable long inventoryId) {

        Member member = rq.getMember();

        RsData<Long> usingRs = inventoryService.usingProduct(inventoryId, member);
        if (usingRs.isFail()) {
            rq.historyBack(usingRs);
        }

        return rq.redirectWithMsg("/member/inventory", usingRs);
    }
}
