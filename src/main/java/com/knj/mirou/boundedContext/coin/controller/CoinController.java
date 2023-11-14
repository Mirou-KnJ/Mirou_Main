package com.knj.mirou.boundedContext.coin.controller;

import com.knj.mirou.boundedContext.coin.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

@Controller
@RequiredArgsConstructor
@RequestMapping("/coin")
public class CoinController {

    private final CoinService coinService;

    @GetMapping("/random")
    @ResponseBody
    public int randomCoin(@RequestParam int maximumCoin) {
        return coinService.randomCoin(maximumCoin);
    }
}