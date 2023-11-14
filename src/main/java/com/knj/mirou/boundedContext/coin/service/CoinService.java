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

        coin = Coin.builder()
                .id(coin.getId())
                .currentCoin(coin.getCurrentCoin() + rewardCoin)
                .totalGetCoin(coin.getTotalGetCoin() + rewardCoin)
                .build();

        coinRepository.save(coin);
    }

}
