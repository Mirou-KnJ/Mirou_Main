package com.knj.mirou.boundedContext.store.controller;

import com.knj.mirou.base.rq.Rq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final Rq rq;


}
