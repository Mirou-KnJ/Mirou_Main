package com.knj.mirou.boundedContext.challengemember.service;

import com.knj.mirou.base.event.EventAfterEndProgress;
import com.knj.mirou.base.event.EventAfterJoinChallenge;
import com.knj.mirou.base.event.EventAfterKickedChallenge;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeDetailDTO;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengemember.config.CMemberConfigProperties;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.challengemember.model.enums.Progress;
import com.knj.mirou.boundedContext.challengemember.repository.ChallengeMemberRepository;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.point.service.PointService;
import com.knj.mirou.boundedContext.reward.model.entity.PrivateReward;
import com.knj.mirou.boundedContext.reward.service.PrivateRewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeMemberService {

    private final PointService pointService;
    private final PrivateRewardService privateRewardService;

    private final CMemberConfigProperties CMemberConfigProps;
    private final ApplicationEventPublisher publisher;

    private final ChallengeMemberRepository challengeMemberRepository;

    @Transactional
    public RsData<Long> join(Challenge challenge, Member member) {

        RsData<Long> usingPointRs = pointService.usingJoinPoint(member, challenge);
        if (usingPointRs.isFail()) {
            return usingPointRs;
        }

        ChallengeMember challengeMember = ChallengeMember.builder()
                .linkedChallenge(challenge)
                .linkedMember(member)
                .progress(Progress.IN_PROGRESS)
                .endDate(calcEndDate(challenge.getPeriod()))
                .build();

        ChallengeMember savedChallengeMember = challengeMemberRepository.save(challengeMember);
        privateRewardService.create(challenge, savedChallengeMember);

        publisher.publishEvent(new EventAfterJoinChallenge(this, savedChallengeMember));

        return RsData.of("S-1", "챌린지에 성공적으로 참여하였습니다.", savedChallengeMember.getId());
    }

    @Transactional
    @Scheduled(cron = "3 0 0 * * ?")
    public void setEndForTargetCM() {

        LocalDate yesterDay = LocalDate.now().minusDays(1);

        List<ChallengeMember> endTargetChallengeMembers =
                challengeMemberRepository.findByEndDateAndProgress(yesterDay, Progress.IN_PROGRESS);

        for (ChallengeMember target : endTargetChallengeMembers) {
            target.finishChallenge();
            publisher.publishEvent(new EventAfterEndProgress(this, target));
        }
    }

    public ChallengeDetailDTO getDetailData(Challenge challenge, Member member, ChallengeDetailDTO detailDTO) {

        detailDTO.setCanJoin(canJoin(member));
        detailDTO.setMemberCount(getCountByLinkedChallenge(challenge));

        Optional<ChallengeMember> OChallengeMember = getByChallengeAndMember(challenge, member);
        if (OChallengeMember.isEmpty()) {
            detailDTO.setJoin(false);
            detailDTO.setPublicRewards(challenge.getPublicReward());

            return detailDTO;
        }

        ChallengeMember challengeMember = OChallengeMember.get();
        List<PrivateReward> privateReward = challengeMember.getPrivateReward();
        detailDTO.setJoin(true);
        detailDTO.setSuccessNum(challengeMember.getSuccessNumber());
        detailDTO.setMaxNum(privateReward.get(privateReward.size() - 1).getRound());
        detailDTO.setLastDayNum(challengeMember.getLastDayNumber());
        detailDTO.setPrivateRewards(challengeMember.getPrivateReward());

        return detailDTO;
    }

    public LocalDate calcEndDate(int challengePeriod) {
        return LocalDate.now().plusDays(challengePeriod);
    }

    public int getCountByLinkedMemberAndProgress(Member member, Progress progress) {
        return challengeMemberRepository.countByLinkedMemberAndProgress(member, progress);
    }

    public Optional<ChallengeMember> getByChallengeAndMember(Challenge linkedChallenge, Member linkedMember) {
        return challengeMemberRepository.findByLinkedChallengeAndLinkedMember(linkedChallenge, linkedMember);
    }

    public boolean canJoin(Member member) {

        int count = challengeMemberRepository.countByLinkedMemberAndProgress(member, Progress.IN_PROGRESS);

        if (count >= CMemberConfigProps.getJoinLimit()) {
            return false;
        }
        return true;
    }

    @Transactional
    public int updateSuccess(ChallengeMember challengeMember) {
        return challengeMember.success();
    }

    @Transactional
    public void finishChallenge(ChallengeMember challengeMember) {
        challengeMember.finishChallenge();
        publisher.publishEvent(new EventAfterEndProgress(this, challengeMember));
    }

    public int getCountByLinkedChallenge(Challenge challenge) {
        return challengeMemberRepository.countByLinkedChallenge(challenge);
    }

    public List<ChallengeMember> getInProgressInfos(Member linkedMember) {

        List<ChallengeMember> myInProgressInfos =
                challengeMemberRepository.findAllByLinkedMemberAndProgress(linkedMember, Progress.IN_PROGRESS);

        return myInProgressInfos;
    }

    public List<Challenge> getMyCompletedChallenges(Member linkedMember) {

        List<ChallengeMember> completedInfos =
                challengeMemberRepository.findAllByLinkedMemberAndProgress(linkedMember, Progress.PROGRESS_END);

        List<Challenge> completedChallenges = new ArrayList<>();

        for (ChallengeMember cm : completedInfos) {
            completedChallenges.add(cm.getLinkedChallenge());
        }

        return completedChallenges;
    }

    public Map<Long, Integer> getWeeklyJoinCounts(List<Challenge> challenges) {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startDayOfWeek = now.minus(6,
                ChronoUnit.DAYS).withHour(0).withMinute(0).withSecond(0);

        Map<Long, Integer> joinCounts = new HashMap<>();

        for (Challenge challenge : challenges) {
            int count = challengeMemberRepository
                    .countByLinkedChallengeAndCreateDateBetween(challenge, startDayOfWeek, now);
            joinCounts.put(challenge.getId(), count);
        }

        return joinCounts;
    }

    @Transactional
    public RsData<String> kickUser(ChallengeMember challengeMember) {

        if (challengeMember.getProgress().equals(Progress.PROGRESS_END)) {
            return RsData.of("F-3", "이미 참여가 종료된 사용자입니다.");
        }

        challengeMember.finishChallenge();

        publisher.publishEvent(new EventAfterKickedChallenge(this, challengeMember));

        return RsData.of("S-1", "추방 처리가 완료되었습니다.");
    }
}
