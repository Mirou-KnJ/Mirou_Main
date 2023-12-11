package com.knj.mirou.boundedContext.coinhistory.repository;

import com.knj.mirou.base.enums.ChangeType;
import com.knj.mirou.boundedContext.coinhistory.entity.CoinHistory;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CoinHistoryRepository extends JpaRepository<CoinHistory, Long> {

    List<CoinHistory> findAllByLinkedMemberOrderByCreateDateDesc(Member linkedMember);

    List<CoinHistory> findAllByChangeTypeAndCreateDateBetween(ChangeType changeType,
                                                              LocalDateTime startOfWeek, LocalDateTime endOfWeek);
}
