package com.knj.mirou.boundedContext.challengemember.service;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ChallengeMemberServiceTest {

    @Autowired
    private ChallengeMemberService challengeMemberService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ChallengeService challengeService;

    private Member testMember1;
    private Member testMember2;
    private Challenge testChallenge1;
    private Challenge testChallenge2;

    @BeforeEach
    void setUp() {

        Optional<Member> OMember1 = memberService.getById(1L);
        if(OMember1.isPresent()) {
            testMember1 = OMember1.get();
        }

        Optional<Member> OMember2 = memberService.getById(2L);
        if(OMember2.isPresent()) {
            testMember2 = OMember2.get();
        }

        Optional<Challenge> OChallenge1 = challengeService.getById(3L);
        if(OChallenge1.isPresent()) {
            testChallenge1 = OChallenge1.get();
        }

        Optional<Challenge> OChallenge2 = challengeService.getById(2L);
        if(OChallenge2.isPresent()) {
            testChallenge2 = OChallenge2.get();
        }
    }

}