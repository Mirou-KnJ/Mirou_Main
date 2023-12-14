package com.knj.mirou.boundedContext.pointhistory.repository;

import com.knj.mirou.base.enums.ChangeType;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.pointhistory.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findAllByLinkedMemberOrderByCreateDateDesc(Member linkedMember);

    List<PointHistory> findAllByChangeTypeAndCreateDateBetween(ChangeType changeType,
                                                               LocalDateTime startOfWeek, LocalDateTime endOfWeek);

    @Query("SELECT DISTINCT p.linkedMember.id FROM PointHistory p")
    List<Long> findDistinctMemberIds();
}
