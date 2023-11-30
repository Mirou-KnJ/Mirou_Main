package com.knj.mirou.boundedContext.store.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.productinfo.model.entity.ProductInfo;
import com.knj.mirou.boundedContext.store.model.entity.Store;
import com.knj.mirou.boundedContext.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final Rq rq;
    private final StoreService storeService;

    @GetMapping("/add")
    public String showAddPage(Model model) {

        List<ProductInfo> productInfos = storeService.getAllProducts();

        model.addAttribute("products", productInfos);

        return "/view/store/addForm";
    }

    @PostMapping("/add")
    public String tryAdd(long productId, String saleType) {

        RsData<String> createRs = storeService.create(productId, saleType);
        if(createRs.isFail()) {
            return rq.historyBack(createRs);
        }

        return rq.redirectWithMsg("/store/add", createRs);
    }

    @GetMapping("/storePage")
    public String storePage(Model model) {

        List<Store> storeProducts = storeService.getAllStoreProduct();

        model.addAttribute("storeProducts", storeProducts);

        return "/view/store/storePage";
    }

}
