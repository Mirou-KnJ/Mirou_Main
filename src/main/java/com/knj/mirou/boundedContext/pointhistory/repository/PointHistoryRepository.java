package com.knj.mirou.boundedContext.pointhistory.repository;

import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.pointhistory.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findAllByLinkedMemberOrderByCreateDateDesc(Member linkedMember);
}
