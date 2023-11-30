package com.knj.mirou.boundedContext.product.model.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.product.model.enums.ProductStatus;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public void setStatus(ProductStatus status) {
        this.status = status;
    }
}
