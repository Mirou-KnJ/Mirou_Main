package com.knj.mirou.boundedContext.member;

import com.knj.mirou.boundedContext.coin.entity.Coin;
import com.knj.mirou.boundedContext.point.Point;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    private long id;
    private String loginId; //로그인 아이디
    private String nickname; //닉네임
    private String socialCode; //로그인 경로
    private String role; //권한
    private String inviteCode; //초대코드
    private Date createdAt; //생성된 시간

    @OneToOne(mappedBy = "owner")
    private Coin coin;

    //프로필 이미지
}
