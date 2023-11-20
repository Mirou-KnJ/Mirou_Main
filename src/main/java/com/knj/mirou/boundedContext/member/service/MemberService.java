package com.knj.mirou.boundedContext.member.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.coin.service.CoinService;
import com.knj.mirou.boundedContext.member.config.MemberConfigProperties;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.model.enums.MemberRole;
import com.knj.mirou.boundedContext.member.model.enums.SocialCode;
import com.knj.mirou.boundedContext.member.repository.MemberRepository;
import com.knj.mirou.boundedContext.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final PointService pointService;
    private final CoinService coinService;
    private final MemberConfigProperties memberConfigProps;
    private final MemberRepository memberRepository;

    public Optional<Member> getByLoginId(String loginId) {

        return memberRepository.findByLoginId(loginId);
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
                .inviteCode("123456789")                      //FIXME: 중복되지 않는 난수 처리
                .coin(coinService.createCoin())
                .point(pointService.createPoint())
                .build();

        Member joinMember = memberRepository.save(member);

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
}
