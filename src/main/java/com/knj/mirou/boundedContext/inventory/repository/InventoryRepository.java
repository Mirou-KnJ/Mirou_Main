package com.knj.mirou.boundedContext.inventory.repository;

import com.knj.mirou.boundedContext.inventory.model.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
