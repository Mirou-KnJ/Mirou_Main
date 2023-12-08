package com.knj.mirou.boundedContext.reportHistory.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challengefeed.model.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
import com.knj.mirou.boundedContext.reportHistory.entity.ReportHistory;
import com.knj.mirou.boundedContext.reportHistory.repository.ReportHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportHistoryService {

    private final ChallengeFeedService challengeFeedService;
    private final ReportHistoryRepository reportHistoryRepository;

    @Transactional
    public RsData<Long> tryReport(long feedId, long reporterId, String contents) {

        Optional<ReportHistory> OHistory = reportHistoryRepository.findByTargetFeedIdAndReporterId(feedId, reporterId);
        if(OHistory.isPresent()){
            return RsData.of("F-1", "이미 신고한 게시물입니다.");
        }

        Optional<ChallengeFeed> OFeed = challengeFeedService.getById(feedId);
        if(OFeed.isEmpty()) {
            return RsData.of("F-2", "유효하지 않은 피드입니다.");
        }

        //FIXME: 여기서 처리하기 보다는 신고수 일정 이상 되었을때의 내용을 challengeFeedServic에 작성하여 호출하기로.
        // (Transaction 섞이지 않게)
        ChallengeFeed challengeFeed = OFeed.get();
        challengeFeed.updateReportCount();

        ReportHistory report = new ReportHistory(feedId, reporterId, contents);
        reportHistoryRepository.save(report);

        return RsData.of("S-1", "신고가 완료되었습니다.", report.getId());
    }

}
