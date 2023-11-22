package com.knj.mirou.boundedContext.reportHistory.repository;

import com.knj.mirou.boundedContext.reportHistory.entity.ReportHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportHistoryRepository extends JpaRepository<ReportHistory, Long> {
}
