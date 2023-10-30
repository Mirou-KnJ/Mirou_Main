package com.knj.mirou.boundedContext.point;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Point {
    @Id

    private long id;
    private long linkedMemberId; // MemberId와 연결
    private int currentPoint; //현재 포인트
    private int totalGetPoint; //누적 획득 포인트
    private int totalUserPoint; //누적 사용 포인트
    private Date createdAt; //생성된 시간
    private Date modifiedAt; //수정된 시간

}
