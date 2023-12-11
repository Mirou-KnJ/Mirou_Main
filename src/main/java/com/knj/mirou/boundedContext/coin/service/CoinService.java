package com.knj.mirou.boundedContext.coin.service;

import com.knj.mirou.base.enums.ChangeType;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.coin.config.CoinConfigProperties;
import com.knj.mirou.boundedContext.coin.entity.Coin;
import com.knj.mirou.boundedContext.coin.repository.CoinRepository;
import com.knj.mirou.boundedContext.coinhistory.service.CoinHistoryService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.product.model.entity.ProductInfo;
import com.knj.mirou.boundedContext.reward.model.entity.PrivateReward;
import com.knj.mirou.boundedContext.reward.service.PrivateRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoinService {

    private final ChallengeMemberService challengeMemberService;
    private final CoinHistoryService coinHistoryService;
    private final PrivateRewardService privateRewardService;

    private final CoinConfigProperties coinConfigProps;
    private final ApplicationEventPublisher publisher;

    private final CoinRepository coinRepository;

    @Transactional
    public Coin create() {
        Coin createCoin = Coin.builder()
                .currentCoin(0)
                .totalGetCoin(0)
                .totalUsedCoin(0)
                .build();

        return coinRepository.save(createCoin);
    }

    @Transactional
    public void giveCoin(Member member, PrivateReward reward, String contents, String imgUrl) {

        Coin coin = member.getCoin();

        int maxReward = Integer.parseInt(reward.getReward());
        int randomCoin = (int) getRandomCoin(maxReward);

        coin = Coin.builder()
                .id(coin.getId())
                .currentCoin(coin.getCurrentCoin() + randomCoin)
                .totalGetCoin(coin.getTotalGetCoin() + randomCoin)
                .totalUsedCoin(coin.getTotalUsedCoin())
                .build();

        coinRepository.save(coin);

        coinHistoryService.create(member, ChangeType.GET, randomCoin, contents + " 적립", imgUrl);
    }

    @Transactional
    public void buyProduct(ProductInfo productInfo, Member member) {

        Coin coin = member.getCoin();
        int cost = productInfo.getCost();

        coin = Coin.builder()
                .id(coin.getId())
                .currentCoin(coin.getCurrentCoin() - cost)
                .totalGetCoin(coin.getTotalGetCoin())
                .totalUsedCoin(coin.getTotalUsedCoin() + cost)
                .build();

        coinRepository.save(coin);

        coinHistoryService.create(member, ChangeType.USED, cost,
                productInfo.getName() + " 구매", productInfo.getImgUrl());
    }

    private double getRandomCoin(int maxReward) {

        Random random = new Random();

        double quarter = coinConfigProps.getQuarter();
        double rewardCoin = maxReward * quarter;

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

    @Transactional
    public void checkReward(ChallengeMember challengeMember) {

        Challenge linkedChallenge = challengeMember.getLinkedChallenge();
        Member linkedMember = challengeMember.getLinkedMember();

        int successNum = challengeMemberService.updateSuccess(challengeMember);

        RsData<PrivateReward> validRewardRs =
                privateRewardService.getValidReward(linkedChallenge, challengeMember, successNum);

        if (validRewardRs.isSuccess()) {
            giveCoin(linkedMember, validRewardRs.getData(), linkedChallenge.getName(), linkedChallenge.getImgUrl());
        }

        if (validRewardRs.getResultCode().contains("S-2")) {
            challengeMemberService.finishChallenge(challengeMember);
        }
    }

    public void givePenalty(Member member, Challenge challenge) {

        Coin coin = member.getCoin();
        int penalty = coinConfigProps.getPenalty();

        coin = Coin.builder()
                .id(coin.getId())
                .currentCoin(coin.getCurrentCoin() - penalty)
                .totalGetCoin(coin.getTotalGetCoin())
                .totalUsedCoin(coin.getTotalUsedCoin() + penalty)
                .build();

        coinRepository.save(coin);

        coinHistoryService.create(member, ChangeType.USED, penalty,
                challenge.getName() + " 신고 패널티", challenge.getImgUrl());
    }
}





