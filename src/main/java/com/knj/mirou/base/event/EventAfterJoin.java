package com.knj.mirou.base.event;

import com.knj.mirou.boundedContext.member.model.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterJoin extends ApplicationEvent {

    private final Member member;

    public EventAfterJoin(Object source, Member member) {
        super(source);
        this.member = member;
    }
}
