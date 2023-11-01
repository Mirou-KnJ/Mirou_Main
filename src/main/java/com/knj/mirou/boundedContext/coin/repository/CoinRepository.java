package com.knj.mirou.boundedContext.coin.repository;

import com.knj.mirou.boundedContext.coin.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, Long> {
}
