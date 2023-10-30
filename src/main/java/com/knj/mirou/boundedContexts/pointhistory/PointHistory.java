package com.knj.mirou.boundedContexts.pointhistory;

import com.knj.mirou.boundedContexts.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.security.Principal;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PointHistory {
    @Id

    private long id;
    private long linkedMemberId;
    private int changePoint; //바뀐 포인트
    private String PointContents; //바뀐 내역
    private Date createdAt; //생성된 시간
}
