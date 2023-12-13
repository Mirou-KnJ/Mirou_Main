package com.knj.mirou.boundedContext.member.repository;

import com.knj.mirou.boundedContext.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(String loginId);

    List<Member> findByPointCurrentPointLessThan(int resetStandard);

    Optional<Member> findByNickname(String nickName);
}
