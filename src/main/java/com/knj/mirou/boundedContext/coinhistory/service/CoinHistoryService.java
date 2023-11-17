package com.knj.mirou.boundedContext.coinhistory.service;

import com.knj.mirou.boundedContext.coinhistory.repository.CoinHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoinHistoryService {

    private final CoinHistoryRepository coinHistoryRepository;
}
