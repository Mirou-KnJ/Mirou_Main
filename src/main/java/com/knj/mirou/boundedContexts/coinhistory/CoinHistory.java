package com.knj.mirou.boundedContexts.coinhistory;

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
    private int changeCoin; //바뀐 포인트
    private String changeCoinContents; //바뀐 내역
    private Date createdAt; //생성된 시간
}
