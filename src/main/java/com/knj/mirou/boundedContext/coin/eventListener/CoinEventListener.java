package com.knj.mirou.boundedContext.coin.eventListener;

import com.knj.mirou.base.event.EventAfterWriteFeed;
import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoinEventListener {

    private final ChallengeFeedService challengeFeedService;

    @EventListener
    @Transactional
    public void listen(EventAfterWriteFeed event) {
        log.info("글이 써졌어요!");
    }
}
