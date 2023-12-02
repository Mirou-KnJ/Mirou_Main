package com.knj.mirou.boundedContext.inventory.service;

import com.knj.mirou.boundedContext.inventory.entity.Inventory;
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

        Inventory newItem = new Inventory(member, product, expDate);

        inventoryRepository.save(newItem);
    }

}
