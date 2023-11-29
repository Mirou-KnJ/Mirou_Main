package com.knj.mirou.boundedContext.store.model.entity;


import com.knj.mirou.boundedContext.product.model.entity.Product;
import com.knj.mirou.boundedContext.store.model.enums.SaleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class Store {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Enumerated(EnumType.STRING)
    private SaleType saleType;
}
