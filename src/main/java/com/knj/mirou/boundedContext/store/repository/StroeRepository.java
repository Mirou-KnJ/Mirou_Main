package com.knj.mirou.boundedContext.store.repository;

import com.knj.mirou.boundedContext.store.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StroeRepository extends JpaRepository<Store, Long> {
}
