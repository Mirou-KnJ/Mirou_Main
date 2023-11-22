package com.knj.mirou.boundedContext.challengemember.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengemember.config.CMemberConfigProperties;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.challengemember.model.enums.Progress;
import com.knj.mirou.boundedContext.challengemember.repository.ChallengeMemberRepository;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
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

    @Transactional
    public RsData<String> join(Challenge linkedChallenge, String loginId) {

        Optional<Member> OMember = memberService.getByLoginId(loginId);

        if(OMember.isEmpty()) {
            return RsData.of("F-1", "회원 정보가 유효하지 않습니다.");
        }

        Member linkedMember = OMember.get();

        ChallengeMember challengeMember = ChallengeMember.builder()
                .linkedChallenge(linkedChallenge)
                .linkedMember(linkedMember)
                .progress(Progress.IN_PROGRESS)
                .endDate(calcEndDate(linkedChallenge.getPeriod()))
                .build();

        ChallengeMember savedChallengeMember = challengeMemberRepository.save(challengeMember);
        privateRewardService.create(linkedChallenge, savedChallengeMember);

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
