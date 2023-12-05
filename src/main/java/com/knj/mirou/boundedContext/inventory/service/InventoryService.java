package com.knj.mirou.boundedContext.inventory.service;

import com.knj.mirou.boundedContext.inventory.model.entity.Inventory;
import com.knj.mirou.boundedContext.inventory.model.enums.InventoryStatus;
import com.knj.mirou.boundedContext.inventory.repository.InventoryRepository;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.product.model.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional
    public void create(Product product, Member member) {

        LocalDate expDate = LocalDate.now().plusDays(90);

        Inventory inventory = Inventory.builder()
                .owner(member)
                .product(product)
                .expDate(expDate)
                .status(InventoryStatus.BEFORE_USED)
                .build();

        inventoryRepository.save(inventory);
    }
}
