package com.knj.mirou.boundedContext.coin.eventListener;

import com.knj.mirou.base.event.EventAfterWriteFeed;
import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.coin.service.CoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoinEventListener {

    private final CoinService coinService;

    @EventListener
    @Transactional
    public void listen(EventAfterWriteFeed event) {

        ChallengeMember challengeMember = event.getChallengeMember();

        coinService.checkReward(challengeMember);
    }
}
