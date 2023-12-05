package com.knj.mirou.boundedContext.inventory.repository;

import com.knj.mirou.boundedContext.inventory.model.entity.Inventory;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByIdAndOwner(long id, Member owner);
}
