package com.knj.mirou.base.event;

import com.knj.mirou.boundedContext.member.model.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterResetPoint extends ApplicationEvent {

    private final Member member;

    public EventAfterResetPoint(Object source, Member member) {
        super(source);
        this.member = member;
    }
}
