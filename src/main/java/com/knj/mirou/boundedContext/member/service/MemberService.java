package com.knj.mirou.boundedContext.member.service;

import com.knj.mirou.base.event.EventAfterJoin;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.coin.service.CoinService;
import com.knj.mirou.boundedContext.member.config.MemberConfigProperties;
import com.knj.mirou.boundedContext.member.model.dtos.ChallengeReportDTO;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.model.enums.MemberRole;
import com.knj.mirou.boundedContext.member.model.enums.SocialCode;
import com.knj.mirou.boundedContext.member.repository.MemberRepository;
import com.knj.mirou.boundedContext.point.config.PointConfigProperties;
import com.knj.mirou.boundedContext.point.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final CoinService coinService;
    private final PointService pointService;
    private final ChallengeService challengeService;

    private final PointConfigProperties pointConfigProps;
    private final MemberConfigProperties memberConfigProps;
    private final ApplicationEventPublisher publisher;

    private final MemberRepository memberRepository;

    public Optional<Member> getByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    public Optional<Member> getById(Long id) {
        return memberRepository.findById(id);
    }

    @Transactional
    public RsData<Member> join(String socialCode, String loginId, String nickname) {

        Optional<Member> OByLoginId = getByLoginId(loginId);
        if (OByLoginId.isPresent()) {
            return RsData.of("F-1", "이미 가입된 회원입니다.", OByLoginId.get());
        }

        MemberRole role = memberConfigProps.isAdmin(loginId) ? MemberRole.ADMIN : MemberRole.USER;

        Member member = Member.builder()
                .loginId(loginId)
                .nickname(nickname)
                .socialCode(SocialCode.valueOf(socialCode))
                .role(role)
                .inviteCode("123456789")
                .coin(coinService.create())
                .point(pointService.create())
                .build();

        Member joinMember = memberRepository.save(member);

        publisher.publishEvent(new EventAfterJoin(this, joinMember));

        return RsData.of("S-1", "회원가입이 완료되었습니다.", joinMember);
    }

    public RsData<Member> socialLogin(String socialCode, String loginId, String nickname) {

        Optional<Member> OByLoginId = getByLoginId(loginId);
        if (OByLoginId.isPresent()) {
            return RsData.of("S-1", "로그인 되었습니다(이미 가입된 회원)", OByLoginId.get());
        }

        RsData<Member> joinRs = join(socialCode, loginId, nickname);
        if (joinRs.isFail()) {
            RsData.of("F-1", "가입 및 로그인에 실패하였습니다.");
        }

        return RsData.of("S-2", "로그인 되었습니다.(새로 가입한 회원)", joinRs.getData());
    }

    public List<? extends GrantedAuthority> getGrantedAuthorities(String loginId) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

        if (memberConfigProps.isAdmin(loginId)) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return grantedAuthorities;
    }

    @Transactional
    @Scheduled(cron = "3 0 0 * * 1")
    public void resetPointOnMonday() {
        int resetStandard = pointConfigProps.getResetStandard();

        List<Member> targetMembers = memberRepository.findByPointCurrentPointLessThan(resetStandard);

        pointService.resetPoint(targetMembers);
    }

    public ChallengeReportDTO getChallengeReportDto() {

        ChallengeReportDTO reportDTO = new ChallengeReportDTO();

        reportDTO.setOpenedChallenges(challengeService.getAllByStatus(ChallengeStatus.OPEN));

        return reportDTO;
    }
}
