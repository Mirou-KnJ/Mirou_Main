package com.knj.mirou.boundedContext.challengemember.service;

import com.knj.mirou.base.enums.ChangeType;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengemember.config.CMemberConfigProperties;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.challengemember.model.enums.Progress;
import com.knj.mirou.boundedContext.challengemember.repository.ChallengeMemberRepository;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import com.knj.mirou.boundedContext.point.entity.Point;
import com.knj.mirou.boundedContext.point.service.PointService;
import com.knj.mirou.boundedContext.pointhistory.service.PointHistoryService;
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

    private final CMemberConfigProperties CMemberConfigProps;
    private final PrivateRewardService privateRewardService;
    private final MemberService memberService;
    private final ChallengeMemberRepository challengeMemberRepository;
    private final PointService pointService;
    private final PointHistoryService pointHistoryService;

    @Transactional
    public RsData<String> join(Challenge challenge, Member member) {

        //포인트가 있는지 검사
        Point point = member.getPoint();
        int cost = challenge.getJoinCost();

        if(cost > point.getCurrentPoint()) {
            return RsData.of("F-2", "참가 비용이 부족합니다.");
        }

        pointService.usedPoint(point, cost);
        pointHistoryService.create(member, ChangeType.USED, cost, challenge.getName() + " 사용",
                challenge.getImgUrl());

        //참여비용 만큼의 포인트를 가지고 있는지 검사
        //1. 없으며 에러
        //2. 있으면 포인트 차감 시도 + 차감하면 히스트리 생성과 연결.

        ChallengeMember challengeMember = ChallengeMember.builder()
                .linkedChallenge(challenge)
                .linkedMember(member)
                .progress(Progress.IN_PROGRESS)
                .endDate(calcEndDate(challenge.getPeriod()))
                .build();

        ChallengeMember savedChallengeMember = challengeMemberRepository.save(challengeMember);
        privateRewardService.create(challenge, savedChallengeMember);

        return RsData.of("S-1", "챌린지에 성공적으로 참여하였습니다.");
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

        if(count >= CMemberConfigProps.getJoinLimit()) {
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

    public List<Challenge> getMyValidChallengeList (String loginId){

        Optional<Member> OMember = memberService.getByLoginId(loginId);

        //TODO 사용자가 로그인 했을 때 로그인 정보가 유효한지 검사

        Member member = OMember.get();
        List<ChallengeMember> membersAllList =
                challengeMemberRepository.findByLinkedMemberAndProgress(member, Progress.IN_PROGRESS);

        List<Challenge> inProgressChallenges = new ArrayList<>();

        for(ChallengeMember cm : membersAllList) {
            inProgressChallenges.add(cm.getLinkedChallenge());
        }

        return inProgressChallenges;
    }

    @Transactional
    @Scheduled(cron = "3 0 0 * * ?")
    public void setEndForTargetCM() {

        LocalDate yesterDay = LocalDate.now().minusDays(1);

        List<ChallengeMember> endTargetChallengeMembers =
                challengeMemberRepository.findByEndDateAndProgress(yesterDay, Progress.IN_PROGRESS);

        for(ChallengeMember target : endTargetChallengeMembers) {
            target.finishChallenge();
            log.info(target.getLinkedMember().getLoginId() + "회원의 " + target.getLinkedChallenge().getName()
                    + "에 대한 참여내용이 종료되었습니다.");
        }
    }
}
