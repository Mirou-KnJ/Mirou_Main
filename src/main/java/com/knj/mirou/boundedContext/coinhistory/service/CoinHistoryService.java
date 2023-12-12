package com.knj.mirou.boundedContext.coinhistory.service;

import com.knj.mirou.base.enums.ChangeType;
import com.knj.mirou.base.event.EventAfterGiveCoin;
import com.knj.mirou.boundedContext.coinhistory.entity.CoinHistory;
import com.knj.mirou.boundedContext.coinhistory.repository.CoinHistoryRepository;
import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.dtos.CoinReportDTO;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoinHistoryService {

    private final ImageDataService imageDataService;

    private final ApplicationEventPublisher publisher;

    private final CoinHistoryRepository coinHistoryRepository;

    @Transactional
    public void create(Member member, ChangeType type, int changedCoin, String contents, String imgUrl) {

        String historyImgUrl = imageDataService.getOptimizingUrl(imgUrl, OptimizerOption.HISTORY);

        CoinHistory coinHistory = CoinHistory.builder()
                .linkedMember(member)
                .changedCoin(changedCoin)
                .changeType(type)
                .contents(contents)
                .imgUrl(historyImgUrl)
                .build();

        coinHistoryRepository.save(coinHistory);

        if(type.equals(ChangeType.GET)) {
            publisher.publishEvent(new EventAfterGiveCoin(this, member, contents, imgUrl));
        }
    }

    public List<CoinHistory> getAllOrderedDesc(Member member) {
        return coinHistoryRepository.findAllByLinkedMemberOrderByCreateDateDesc(member);
    }

    public List<CoinHistory> getWeeklyHistoryByType(ChangeType changeType) {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startDayOfWeek = now.minus(7,
                ChronoUnit.DAYS).withHour(0).withMinute(0).withSecond(0);

        LocalDateTime endDayOfWeek = now.minus(1,
                ChronoUnit.DAYS).withHour(23).withMinute(59).withSecond(59);

        List<CoinHistory> coinHistories = coinHistoryRepository
                .findAllByChangeTypeAndCreateDateBetween(changeType, startDayOfWeek, endDayOfWeek);

        return coinHistories;
    }

    public int getSumByHistories(List<CoinHistory> histories) {

        int sum = 0;
        for(CoinHistory history : histories) {
            sum += history.getChangedCoin();
        }

        return sum;
    }

    public long getAverageBySum(int sum) {

        List<Long> distinctMemberIds = coinHistoryRepository.findDistinctMemberIds();

        return sum / distinctMemberIds.size();
    }

    public CoinReportDTO getCoinReportDTO() {

        CoinReportDTO coinReportDTO = new CoinReportDTO();

        List<CoinHistory> weeklyGivenHistories = getWeeklyHistoryByType(ChangeType.GET);
        int weeklyGivenCoinSum = getSumByHistories(weeklyGivenHistories);
        coinReportDTO.setWeeklyGivenCoinSum(weeklyGivenCoinSum);
        coinReportDTO.setWeeklyGivenCoinAverage(getAverageBySum(weeklyGivenCoinSum));

        List<CoinHistory> weeklyUsedHistories = getWeeklyHistoryByType(ChangeType.USED);
        int weeklyUsedCoinSum = getSumByHistories(weeklyUsedHistories);
        coinReportDTO.setWeeklyUsedCoinSum(weeklyUsedCoinSum);
        coinReportDTO.setWeeklyUsedCoinAverage(getAverageBySum(weeklyUsedCoinSum));

        return coinReportDTO;
    }
}
