package com.knj.mirou.boundedContext.product.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.boundedContext.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final Rq rq;
    private final ProductService productService;

    @GetMapping("/add")
    public String addForm() {

        return "view/product/addForm";
    }

    @PostMapping("/add")
    public String addProduct(String name, String brandName, int cost, String content, String usingWay,
                             String usingCaution, MultipartFile img) {

        System.out.println("name = " + name);
        System.out.println("brandName = " + brandName);
        System.out.println("cost = " + cost);
        System.out.println("content = " + content);
        System.out.println("usingWay = " + usingWay);
        System.out.println("usingCaution = " + usingCaution);
        System.out.println("img.getOriginalFilename() = " + img.getOriginalFilename());

        return rq.redirectWithMsg("/product/add", "등록 되었습니다.");
    }

}
