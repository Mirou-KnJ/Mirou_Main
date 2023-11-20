package com.knj.mirou.boundedContext.coinhistory.service;

import com.knj.mirou.base.enums.ChangeType;
import com.knj.mirou.boundedContext.coinhistory.entity.CoinHistory;
import com.knj.mirou.boundedContext.coinhistory.repository.CoinHistoryRepository;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoinHistoryService {

    private final CoinHistoryRepository coinHistoryRepository;

    @Transactional
    public void create(Member linkedMember, ChangeType type, int changedCoin, String contents) {

        CoinHistory coinHistory = CoinHistory.builder()
                .linkedMember(linkedMember)
                .changedCoin(changedCoin)
                .changeType(type)
                .contents(contents)
                .build();

        coinHistoryRepository.save(coinHistory);
    }
}
