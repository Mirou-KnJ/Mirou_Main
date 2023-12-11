package com.knj.mirou.boundedContext.reportHistory.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challengefeed.model.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.challengefeed.service.ChallengeFeedService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import com.knj.mirou.boundedContext.reportHistory.entity.ReportHistory;
import com.knj.mirou.boundedContext.reportHistory.repository.ReportHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportHistoryService {

    private final MemberService memberService;
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

        Optional<Member> OMember = memberService.getById(reporterId);
        if(OMember.isEmpty()) {
            return RsData.of("F-3", "신고자 정보가 유효하지 않습니다.");
        }

        ChallengeFeed targetFeed = OFeed.get();
        Member reporter = OMember.get();

        ReportHistory report = new ReportHistory(targetFeed, reporter, targetFeed.getWriter(), contents);
        reportHistoryRepository.save(report);

        challengeFeedService.updateReportCount(targetFeed);

        return RsData.of("S-1", "신고가 완료되었습니다.", report.getId());
    }


    public int getWeeklyReportedCounts(Member member) {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startDayOfWeek = now.minus(7,
                ChronoUnit.DAYS).withHour(0).withMinute(0).withSecond(0);

        log.info("start : " + startDayOfWeek.toString());

        LocalDateTime endDayOfWeek = now.minus(1,
                ChronoUnit.DAYS).withHour(0).withMinute(0).withSecond(0);

        log.info("end : " + endDayOfWeek.toString());


        int count = reportHistoryRepository
                .countByReportedMemberAndCreateDateBetween(member, startDayOfWeek, endDayOfWeek);

        log.error("count : " + count);

        return count;
    }

}
