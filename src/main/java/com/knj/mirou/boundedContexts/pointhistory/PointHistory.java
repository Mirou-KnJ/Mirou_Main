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
    private int changePoint;
    private boolean changeType;
    private boolean historyType;
    private Date createdAt;
}
