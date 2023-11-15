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

        //TODO: 확률 조정 알고리즘
        int randomCoin = randomCoin(rewardCoin);

        //TODO: 지급 히스토리 기록

        coin = Coin.builder()
                .id(coin.getId())
                .currentCoin(coin.getCurrentCoin() + rewardCoin)
                .totalGetCoin(coin.getTotalGetCoin() + rewardCoin)
                .build();

        coinRepository.save(coin);
    }

    private int randomCoin(int rewardCoin) {
        Random random = new Random();
        int quarter = rewardCoin / 4;
        int rand = random.nextInt(rewardCoin);

        if (rand < quarter) {
            return (int)(rewardCoin * 0.4);
        } else if (rand < 2 * quarter) {
            return (int)(rewardCoin * 0.3);
        } else if (rand < 3 * quarter) {
            return (int)(rewardCoin * 0.2);
        } else {
            return (int)(rewardCoin * 0.1);
        }
    }

}





