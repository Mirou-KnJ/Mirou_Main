package com.knj.mirou.boundedContext.product.controller;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeLabel;
import com.knj.mirou.boundedContext.product.model.entity.Product;
import com.knj.mirou.boundedContext.product.model.entity.ProductInfo;
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

    private final ProductService productService;

    @GetMapping("/codeForm")
    public String codeForm(Model model) {

        List<ProductInfo> productInfos = productService.getAllProductInfo();

        model.addAttribute("productInfos", productInfos);

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
}
