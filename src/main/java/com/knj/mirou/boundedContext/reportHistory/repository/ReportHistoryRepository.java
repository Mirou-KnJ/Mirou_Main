package com.knj.mirou.boundedContext.reportHistory.repository;

import com.knj.mirou.boundedContext.reportHistory.entity.ReportHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportHistoryRepository extends JpaRepository<ReportHistory, Long> {

    Optional<ReportHistory> findByTargetFeedIdAndReporterId(long targetFeedId, long reporterId);
}
