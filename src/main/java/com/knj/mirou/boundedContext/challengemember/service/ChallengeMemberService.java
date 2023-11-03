package com.knj.mirou.boundedContext.challengemember.service;

import com.knj.mirou.boundedContext.challengemember.repository.ChallengeMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeMemberService {

    private final ChallengeMemberRepository challengeMemberRepository;

}
