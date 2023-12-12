package com.knj.mirou.boundedContext.pointhistory.service;

import com.knj.mirou.base.enums.ChangeType;
import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.dtos.CurrencyReportDTO;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.pointhistory.entity.PointHistory;
import com.knj.mirou.boundedContext.pointhistory.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointHistoryService {

    private final ImageDataService imageDataService;

    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public void create(Member linkedMember, ChangeType type, int changedPoint, String contents, String imgUrl){

        String historyImgUrl = imageDataService.getOptimizingUrl(imgUrl, OptimizerOption.HISTORY);

        PointHistory pointHistory = PointHistory.builder()
                .linkedMember(linkedMember)
                .changedPoint(changedPoint)
                .changeType(type)
                .contents(contents)
                .imgUrl(historyImgUrl)
                .build();

        pointHistoryRepository.save(pointHistory);
    }

    public List<PointHistory> getAllOrderedDesc(Member member){
        return pointHistoryRepository.findAllByLinkedMemberOrderByCreateDateDesc(member);
    }

    public List<PointHistory> getWeeklyHistoryByType(ChangeType changeType) {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startDayOfWeek = now.minus(7,
                ChronoUnit.DAYS).withHour(0).withMinute(0).withSecond(0);

        LocalDateTime endDayOfWeek = now.minus(1,
                ChronoUnit.DAYS).withHour(23).withMinute(59).withSecond(59);

        List<PointHistory> pointHistories = pointHistoryRepository
                .findAllByChangeTypeAndCreateDateBetween(changeType, startDayOfWeek, endDayOfWeek);

        return pointHistories;
    }

    public int getSumByHistories(List<PointHistory> histories) {

        int sum = 0;
        for(PointHistory history : histories) {
            sum += history.getChangedPoint();
        }

        return sum;
    }

    public long getAverageBySum(int sum) {

        List<Long> distinctMemberIds = pointHistoryRepository.findDistinctMemberIds();

        return sum / distinctMemberIds.size();
    }

    public CurrencyReportDTO getPointReportDTO() {

        CurrencyReportDTO reportDTO = new CurrencyReportDTO();

        List<PointHistory> weeklyGivenHistories = getWeeklyHistoryByType(ChangeType.GET);
        int weeklyGivenPointSum = getSumByHistories(weeklyGivenHistories);
        reportDTO.setWeeklyGivenCurrencySum(weeklyGivenPointSum);
        reportDTO.setWeeklyGivenCurrencyAverage(getAverageBySum(weeklyGivenPointSum));

        List<PointHistory> weeklyUsedHistories = getWeeklyHistoryByType(ChangeType.USED);
        int weeklyUsedPointSum = getSumByHistories(weeklyUsedHistories);
        reportDTO.setWeeklyUsedCurrencySum(weeklyUsedPointSum);
        reportDTO.setWeeklyUsedCurrencyAverage(getAverageBySum(weeklyUsedPointSum));

        return reportDTO;
    }
}
