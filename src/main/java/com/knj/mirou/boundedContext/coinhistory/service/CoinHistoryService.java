package com.knj.mirou.boundedContext.coinhistory.service;

import com.knj.mirou.base.enums.ChangeType;
import com.knj.mirou.boundedContext.coinhistory.entity.CoinHistory;
import com.knj.mirou.boundedContext.coinhistory.repository.CoinHistoryRepository;
import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoinHistoryService {

    private final ImageDataService imageDataService;

    private final CoinHistoryRepository coinHistoryRepository;

    @Transactional
    public void create(Member member, ChangeType type, int changedCoin, String contents, String imgUrl) {

        String historyImgUrl = imageDataService.getOptimizingUrl(imgUrl, OptimizerOption.HISTORY);

        CoinHistory coinHistory = CoinHistory.builder()
                .linkedMember(member)
                .changedCoin(changedCoin)
                .changeType(type)
                .contents(contents)
                .imgUrl(historyImgUrl)
                .build();

        coinHistoryRepository.save(coinHistory);
    }

    public List<CoinHistory> getAllOrderedDesc(Member member) {
        return coinHistoryRepository.findAllByLinkedMemberOrderByCreateDateDesc(member);
    }
}
