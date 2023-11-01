package com.knj.mirou.boundedContext.member.service;

import com.knj.mirou.boundedContext.coin.service.CoinService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.repository.MemberRepository;
import com.knj.mirou.boundedContext.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PointService pointService;
    private final CoinService coinService;

    public Optional<Member> getByLoginId(String loginId) {

        return memberRepository.findByLoginId(loginId);
    }

    @Transactional
    public Map<String, String> join(String socialCode, String loginId, String password) {

        Map<String, String> joinResultMap = new HashMap<>();

        if(getByLoginId(loginId).isPresent()) {
            joinResultMap.put("ResultCode", "F-1");
            return joinResultMap;
        } else {

            Member member = Member.builder()
                    .point(pointService.createPoint())
                    .coin(coinService.createCoin())
                    .build();


            memberRepository.save(member);
            joinResultMap.put("ResultCode", "S-1");
            return joinResultMap;
        }
    }

}
