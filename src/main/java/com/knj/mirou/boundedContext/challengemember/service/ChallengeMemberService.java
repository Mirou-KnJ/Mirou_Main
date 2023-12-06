package com.knj.mirou.boundedContext.challengemember.service;

import com.knj.mirou.base.enums.ChangeType;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.dtos.ChallengeDetailDTO;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengemember.config.CMemberConfigProperties;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.challengemember.model.enums.Progress;
import com.knj.mirou.boundedContext.challengemember.repository.ChallengeMemberRepository;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.point.entity.Point;
import com.knj.mirou.boundedContext.point.service.PointService;
import com.knj.mirou.boundedContext.pointhistory.service.PointHistoryService;
import com.knj.mirou.boundedContext.reward.model.entity.PrivateReward;
import com.knj.mirou.boundedContext.reward.service.PrivateRewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeMemberService {

    private final PointService pointService;
    private final PrivateRewardService privateRewardService;

    private final CMemberConfigProperties CMemberConfigProps;

    private final ChallengeMemberRepository challengeMemberRepository;

    @Transactional
    public RsData<Long> join(Challenge challenge, Member member) {

        RsData<Long> usingPointRs = pointService.usingJoinPoint(member, challenge);
        if(usingPointRs.isFail()) {
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
}
