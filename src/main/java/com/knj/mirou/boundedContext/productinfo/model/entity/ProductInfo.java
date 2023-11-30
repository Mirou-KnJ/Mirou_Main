package com.knj.mirou.boundedContext.productinfo.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class ProductInfo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    private String name;

    private String brandName;

    private int cost;

    private String content;

    private String imgUrl;

    private String usingWay;

    private String usingCaution;
}
