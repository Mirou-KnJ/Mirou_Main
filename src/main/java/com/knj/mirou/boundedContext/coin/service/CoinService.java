package com.knj.mirou.boundedContext.coin.service;

import com.knj.mirou.boundedContext.coin.entity.Coin;
import com.knj.mirou.boundedContext.coin.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoinService {

    private final CoinRepository coinRepository;

    @Transactional
    public Coin createCoin() {
        Coin createCoin = Coin.builder()
                .currentCoin(0)
                .totalGetCoin(0)
                .totalUsedCoin(0)
                .build();

        return coinRepository.save(createCoin);
    }

    public int randomCoin(int maximumCoin) {
        Random random = new Random();
        int result = 0;

        double probability = random.nextDouble();

        if (probability < 0.4) {
            result = random.nextInt((int) (maximumCoin / 4.0));
        } else if (probability < 0.7) {
            result = random.nextInt((int) (maximumCoin / 4.0)) + (int) (maximumCoin / 4.0);
        } else if (probability < 0.9) {
            result = random.nextInt((int) (maximumCoin / 4.0)) + (int) (maximumCoin / 2.0);
        } else {
            result = random.nextInt((int) (maximumCoin / 4.0)) + (int) (3 * maximumCoin / 4.0);
        }

        return result;
    }
}