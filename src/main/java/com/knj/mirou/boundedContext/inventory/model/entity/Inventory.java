package com.knj.mirou.boundedContext.inventory.model.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.inventory.model.enums.InventoryStatus;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.product.model.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

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
    private Product product;

    private LocalDate expDate;

    @Enumerated(EnumType.STRING)
    private InventoryStatus status;

    private void usingProduct() {
        this.status = InventoryStatus.AFTER_USED;
    }
}
