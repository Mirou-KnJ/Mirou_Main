package com.knj.mirou.boundedContext.store.controller;

import com.knj.mirou.boundedContext.product.model.entity.Product;
import com.knj.mirou.boundedContext.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/add")
    public String showAddPage(Model model) {

        List<Product> products = storeService.getAllProducts();

        model.addAttribute("products", products);

        return "/view/store/addForm";
    }

}