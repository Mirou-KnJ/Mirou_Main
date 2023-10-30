package com.knj.mirou.boundedContexts.pointhistory;

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
    private int changePoint; //바뀐 포인트
    private String changePointContents; //바뀐 내역
    private Date createdAt; //생성된 시간
}
