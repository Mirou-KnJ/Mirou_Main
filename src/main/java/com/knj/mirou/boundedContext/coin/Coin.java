package com.knj.mirou.boundedContext.coin;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Coin {
    @Id

    private long id;
    private long linkedMemberId;
    private int currentCoin; //현재 코인
    private int totalGetCoin; //누적 획득 코인
    private int totalUserCoin; //누적 사용 코인
    private Date createdAt; //생성된 시간
    private Date modifiedAt; //수정된 시간
}
