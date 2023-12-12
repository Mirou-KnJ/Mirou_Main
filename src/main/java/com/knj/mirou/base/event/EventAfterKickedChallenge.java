package com.knj.mirou.base.event;

import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterKickedChallenge extends ApplicationEvent {

    private final ChallengeMember challengeMember;

    public EventAfterKickedChallenge(Object source, ChallengeMember challengeMember) {
        super(source);
        this.challengeMember = challengeMember;
    }
}
