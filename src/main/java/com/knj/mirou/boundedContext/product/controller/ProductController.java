package com.knj.mirou.boundedContext.product.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeLabel;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.product.model.entity.Product;
import com.knj.mirou.boundedContext.product.model.entity.ProductInfo;
import com.knj.mirou.boundedContext.product.model.enums.ProductStatus;
import com.knj.mirou.boundedContext.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final Rq rq;
    private final ProductService productService;

    @GetMapping("/codeForm")
    public String codeForm(Model model) {

        List<ProductInfo> productInfos = productService.getAllProductInfo();
        List<Integer> counts = productService.getInfoCountList(productInfos);

        model.addAttribute("productInfos", productInfos);
        model.addAttribute("counts", counts);

        return "view/product/codeAddForm";
    }

    @ResponseBody
    @PostMapping("/addCode")
    public ResponseEntity<RsData<Long>> addCode(@RequestParam Map<String, Object> params) {

        long infoId = Long.parseLong(params.get("infoId").toString());
        String code = params.get("code").toString();

        RsData<Long> createRs = productService.create(infoId, code);

        return ResponseEntity.ok(createRs);
    }

    @ResponseBody
    @PostMapping("/startSale")
    public ResponseEntity<RsData<String>> startSale(@RequestParam Map<String, Object> params) {

        long infoId = Long.parseLong(params.get("id").toString());

        RsData<String> startSaleRs = productService.startSale(infoId);

        return ResponseEntity.ok(startSaleRs);
    }

    @GetMapping("/store")
    public String storePage(Model model) {

        //1. 모든 info를 가져온다
        //2. 모든 info의 count도 가져온다.
        // 주의할 것 : count의 리스트 인덱스와 info의 인덱스가 같을것이라는 보장이 없다.

        // => 해결 방안 : info에 카운트도 저장? 혹은 본인의 카운트를 명확히 알 수 있는 방법? (맵으로 리턴?)

        List<ProductInfo> infos = productService.getAllRegisteredInfos();
        Map<Long, Integer> counts = productService.getSalingCountMap(infos);

        model.addAttribute("infos", infos);
        model.addAttribute("counts", counts);

        return "view/product/store";
    }

    @ResponseBody
    @PostMapping("/buy")
    public ResponseEntity<RsData<String>> tryBuy(@RequestParam Map<String, Object> params) {

        Member member = rq.getMember();
        long infoId = Long.parseLong(params.get("id").toString());

        RsData<String> buyRs = productService.tryBuy(infoId, member);

        return ResponseEntity.ok(buyRs);
    }
}
