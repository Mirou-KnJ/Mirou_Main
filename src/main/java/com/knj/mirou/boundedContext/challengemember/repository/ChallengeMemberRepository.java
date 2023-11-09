package com.knj.mirou.boundedContext.challengemember.repository;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengemember.model.entity.ChallengeMember;
import com.knj.mirou.boundedContext.challengemember.model.enums.Progress;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeMemberRepository extends JpaRepository<ChallengeMember, Long> {

    long countByLinkedMemberAndProgress(Member member, Progress progress);

    Optional<ChallengeMember> findByLinkedMember(Member linkedMember);

    Optional<ChallengeMember> findByLinkedChallengeAndLinkedMember(Challenge linkedChallenge, Member linkedMember);

}
