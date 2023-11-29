package com.knj.mirou.boundedContext.product.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.imageData.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final Rq rq;
    private final ProductService productService;
    private final ImageDataService imageDataService;

    @GetMapping("/add")
    public String addForm() {

        return "view/product/addForm";
    }

    @PostMapping("/add")
    public String addProduct(String name, String brandName, int cost, String content, String usingWay,
                             String usingCaution, MultipartFile img) throws IOException {

        String imgUrl = imageDataService.tryUploadImg(img, ImageTarget.PRODUCT_IMG).getData();

        productService.create(name, brandName, cost, content, imgUrl, usingWay, usingCaution);

        return rq.redirectWithMsg("/product/add", "등록 되었습니다.");
    }

}
