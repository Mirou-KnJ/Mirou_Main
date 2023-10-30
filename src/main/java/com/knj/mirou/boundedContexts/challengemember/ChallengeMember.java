package com.knj.mirou.boundedContexts.challengemember;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeMember {
    @Id

    private long id;
    private long linkedChallengeId; //챌린지 아이디 연결
    private long linkedMemberId; //회원 아이디 연결
    private int successNumber; //인증 횟수
}
