package com.knj.mirou.boundedContext.product.model.entity;

import com.knj.mirou.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class Product extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductInfo info;

    private String code;
}
