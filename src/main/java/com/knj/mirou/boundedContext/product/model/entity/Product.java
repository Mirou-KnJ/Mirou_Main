package com.knj.mirou.boundedContext.product.model.entity;


import com.knj.mirou.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class Product {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    private String name;

    private String brandName;

    private int cost;

    private String content;

    private String usingWay;

    private String usingCaution;


}
