package com.knj.mirou.base.event;

import com.knj.mirou.boundedContext.member.model.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterGiveCoin extends ApplicationEvent {

    private final Member member;
    private final String contents;
    private final String imgUrl;

    public EventAfterGiveCoin(Object source, Member member, String contents, String imgUrl) {
        super(source);
        this.member = member;
        this.contents = contents;
        this.imgUrl = imgUrl;
    }
}
