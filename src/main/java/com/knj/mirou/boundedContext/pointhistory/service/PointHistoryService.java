package com.knj.mirou.boundedContext.pointhistory.service;

import com.knj.mirou.base.enums.ChangeType;
import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.service.ImageDataService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.pointhistory.entity.PointHistory;
import com.knj.mirou.boundedContext.pointhistory.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointHistoryService {

    private final ImageDataService imageDataService;

    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public void create(Member linkedMember, ChangeType type, int changedPoint, String contents, String imgUrl){

        String historyImgUrl = imageDataService.getOptimizingUrl(imgUrl, OptimizerOption.HISTORY);

        PointHistory pointHistory = PointHistory.builder()
                .linkedMember(linkedMember)
                .changedPoint(changedPoint)
                .changeType(type)
                .contents(contents)
                .imgUrl(historyImgUrl)
                .build();

        pointHistoryRepository.save(pointHistory);
    }

    public List<PointHistory> getAllOrderedDesc(Member member){
        return pointHistoryRepository.findAllByLinkedMemberOrderByCreateDateDesc(member);
    }
}
