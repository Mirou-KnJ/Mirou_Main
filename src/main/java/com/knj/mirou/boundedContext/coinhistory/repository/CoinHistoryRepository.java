package com.knj.mirou.boundedContext.coinhistory.repository;

import com.knj.mirou.boundedContext.coinhistory.entity.CoinHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinHistoryRepository extends JpaRepository<CoinHistory, Long> {
}
