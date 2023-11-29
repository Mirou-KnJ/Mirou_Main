package com.knj.mirou.boundedContext.store.repository;

import com.knj.mirou.boundedContext.store.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByProductName(String name);
}
