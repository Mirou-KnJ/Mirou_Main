package com.knj.mirou.base.event;

import com.knj.mirou.boundedContext.challengefeed.model.entity.ChallengeFeed;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterWriteFeed extends ApplicationEvent {

    private final ChallengeFeed challengeFeed;

    public EventAfterWriteFeed(Object source, ChallengeFeed challengeFeed) {
        super(source);
        this.challengeFeed = challengeFeed;
    }
}
