package com.knj.mirou.boundedContext.reward.service;

import com.knj.mirou.boundedContext.reward.repository.PublicRewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicRewardService {

    private final PublicRewardRepository publicRewardRepository;


}
