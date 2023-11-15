package com.knj.mirou.boundedContext.coin.service;

import com.knj.mirou.boundedContext.coin.entity.Coin;
import com.knj.mirou.boundedContext.coin.repository.CoinRepository;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.reward.model.entity.PrivateReward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        //TODO: 확률 조정 알고리즘
        double randomCoin = Math.random();

        if (randomCoin < 0.25) {
            rewardCoin = (int) (rewardCoin * 0.4);  // 0 ~ 1/4 구간: 40% 확률
        } else if (randomCoin < 0.5) {
            rewardCoin = (int)(rewardCoin * 0.3);   // 1/4 ~ 1/2 : 30% 확률
        } else if (randomCoin < 0.75) {
            rewardCoin = (int)(rewardCoin * 0.2);   // 1/2 ~ 3/4 : 20% 확률
        } else {
            rewardCoin = (int)(rewardCoin * 0.1);   // 3/4 ~ 100 : 10% 확률
        }

        //TODO: 지급 히스토리 기록

        coin = Coin.builder()
                .id(coin.getId())
                .currentCoin(coin.getCurrentCoin() + rewardCoin)
                .totalGetCoin(coin.getTotalGetCoin() + rewardCoin)
                .build();

        coinRepository.save(coin);
    }

}





