package com.knj.mirou.boundedContext.coin.service;

import com.knj.mirou.boundedContext.coin.entity.Coin;
import com.knj.mirou.boundedContext.coin.repository.CoinRepository;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.reward.model.entity.PrivateReward;
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

    @Transactional
    public void giveCoin(Member member, PrivateReward reward) {

        Coin coin = member.getCoin();
        int rewardCoin = Integer.parseInt(reward.getReward());
        double randomCoin = randomCoin(rewardCoin);
        int randomResult = (int) randomCoin;

        //TODO: 지급 히스토리 기록

        coin = Coin.builder()
                .id(coin.getId())
                .currentCoin(coin.getCurrentCoin() + randomResult)
                .totalGetCoin(coin.getTotalGetCoin() + randomResult)
                .build();

        coinRepository.save(coin);
    }

    private double randomCoin(int rewardCoin) {

        Random random = new Random();
        double quarter = 0.25;
        double[] standard = {0.5, 0.9, 1.0};
        double rand = random.nextDouble(0.0, 1.0);

        if (rand < standard[0]) {
            return random.nextDouble(rewardCoin * quarter, rewardCoin * quarter * 2);
        } else if (rand < standard[1]) {
            return random.nextDouble(rewardCoin * quarter * 2, rewardCoin * quarter * 3);
        } else {
            return random.nextDouble(rewardCoin * quarter * 3, rewardCoin * quarter * 4);
        }
    }

}





