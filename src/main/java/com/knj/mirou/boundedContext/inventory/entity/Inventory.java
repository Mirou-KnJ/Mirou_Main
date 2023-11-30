package com.knj.mirou.boundedContext.inventory.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.productinfo.model.entity.ProductInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class Inventory extends BaseEntity {

    @ManyToOne
    private Member owner;

    @ManyToOne
    private ProductInfo productInfo;

    private LocalDateTime expDate;

    private String couponNumber;

}
