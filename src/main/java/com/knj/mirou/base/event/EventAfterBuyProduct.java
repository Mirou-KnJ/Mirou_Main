package com.knj.mirou.base.event;

import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.product.model.entity.ProductInfo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterBuyProduct extends ApplicationEvent {

    private final Member member;
    private final ProductInfo productInfo;

    public EventAfterBuyProduct(Object source, Member member, ProductInfo productInfo) {
        super(source);
        this.member = member;
        this.productInfo = productInfo;
    }
}
