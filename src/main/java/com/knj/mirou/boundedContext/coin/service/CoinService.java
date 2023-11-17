package com.knj.mirou.boundedContext.coin.service;

import com.knj.mirou.base.enums.ChangeType;
import com.knj.mirou.boundedContext.coin.config.CoinConfigProperties;
import com.knj.mirou.boundedContext.coin.entity.Coin;
import com.knj.mirou.boundedContext.coin.repository.CoinRepository;
import com.knj.mirou.boundedContext.coinhistory.service.CoinHistoryService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.reward.model.entity.PrivateReward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoinService {

    private final CoinConfigProperties coinConfigProps;
    private final CoinHistoryService coinHistoryService;
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

        coin = Coin.builder()
                .id(coin.getId())
                .currentCoin(coin.getCurrentCoin() + randomResult)
                .totalGetCoin(coin.getTotalGetCoin() + randomResult)
                .build();

        coinRepository.save(coin);

        //FIXME: 명확한 적립 경로(contents) 기재
        coinHistoryService.create(member, ChangeType.GET, randomResult, "챌린지 인증 성공 적립");
    }

    private double randomCoin(int baseReward) {

        Random random = new Random();

        double quarter = coinConfigProps.getQuarter();
        double rewardCoin = baseReward * quarter;

        List<Double> standard = coinConfigProps.getStandard();
        double rand = random.nextDouble(0.0, 1.0);

        if (rand < standard.get(0)) {
            return random.nextDouble(rewardCoin, rewardCoin * 2);
        } else if (rand < standard.get(1)) {
            return random.nextDouble(rewardCoin * 2, rewardCoin * 3);
        } else {
            return random.nextDouble(rewardCoin * 3, rewardCoin * 4);
        }
    }

}





