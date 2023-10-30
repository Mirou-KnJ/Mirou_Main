package com.knj.mirou.boundedContext.coinhistory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CoinHistory {
    @Id

    private long id;
    private long linkedMemberId;
    private int changeCoin; //바뀐 포인트
    private String CoinContents; //바뀐 내역
    private Date createdAt; //생성된 시간
}
