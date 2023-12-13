package com.knj.mirou.boundedContext.reportHistory.repository;

import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.reportHistory.entity.ReportHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReportHistoryRepository extends JpaRepository<ReportHistory, Long> {

    Optional<ReportHistory> findByTargetFeedIdAndReporterId(long targetFeedId, long reporterId);

    int countByReportedMemberAndCreateDateBetween(Member reportedMember,
                                                    LocalDateTime startDayOfWeek, LocalDateTime endDayOfWeek);

    List<ReportHistory> findAllByCreateDateBetween(LocalDateTime startOfWeek, LocalDateTime endOfWeek);
}
