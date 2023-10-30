package com.knj.mirou.boundedContexts.member;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    private long id;
    private String loginId; //로그인아이디
    private String nickname; //닉네임
    private String socialCode; //로그인 경로
    private String inventory; //아이템 보관함
    private String role; //권한
    private int point; //일상지원금
    private int coin; //코인
    private Date createdAt; //생성시간

}
