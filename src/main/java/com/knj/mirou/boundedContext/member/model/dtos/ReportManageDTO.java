package com.knj.mirou.boundedContext.member.model.dtos;

import com.knj.mirou.boundedContext.reportHistory.entity.ReportHistory;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
@RequiredArgsConstructor
public class ReportManageDTO {

    public List<ReportHistory> weeklyReportHistories;

}
