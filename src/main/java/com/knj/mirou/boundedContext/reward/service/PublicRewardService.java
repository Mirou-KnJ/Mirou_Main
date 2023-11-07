package com.knj.mirou.boundedContext.reward.service;

import com.knj.mirou.boundedContext.reward.model.entity.PublicReward;
import com.knj.mirou.boundedContext.reward.repository.PublicRewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicRewardService {

    private final PublicRewardRepository publicRewardRepository;

    @Transactional
    public void create() {


    }

    public List<PublicReward> getAllReward() {

        return publicRewardRepository.findAll();
    }

}
