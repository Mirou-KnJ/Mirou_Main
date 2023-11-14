package com.knj.mirou.boundedContext.challengemember.service;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.challengemember.model.enums.Progress;
import com.knj.mirou.boundedContext.challengemember.repository.ChallengeMemberRepository;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeMemberService {

    private final MemberService memberService;
    private final ChallengeService challengeService;
    private final ChallengeMemberRepository challengeMemberRepository;

    @Transactional
    public ChallengeMember join(Challenge linkedChallenge, String loginId) {

        Member linkedMember = memberService.getByLoginId(loginId).get();

        ChallengeMember newChallengeMember = ChallengeMember.builder()
                .linkedChallenge(linkedChallenge)
                .linkedMember(linkedMember)
                .progress(Progress.IN_PROGRESS)
                .endDate(LocalDateTime.now())      //FIXME : 가입날짜 + 챌린지의 period 그리고 LocalDate로 수정필요
                .build();

        return challengeMemberRepository.save(newChallengeMember);
    }

    public Optional<ChallengeMember> getByMember(Member member) {

        return challengeMemberRepository.findByLinkedMember(member);
    }

    public Optional<ChallengeMember> getByChallengeAndMember(Challenge linkedChallenge, Member linkedMember) {

        return challengeMemberRepository.findByLinkedChallengeAndLinkedMember(linkedChallenge, linkedMember);
    }

    public boolean canJoin(Member member) {

        int count = challengeMemberRepository.countByLinkedMemberAndProgress(member, Progress.IN_PROGRESS);

        if(count >= 3) {
            return false;
        }

        return true;
    }

    @Transactional
    public void updateSuccess(Member member, Challenge challenge) {

        ChallengeMember challengeMember = getByChallengeAndMember(challenge, member).get();

        challengeMember.success();
    }

}
