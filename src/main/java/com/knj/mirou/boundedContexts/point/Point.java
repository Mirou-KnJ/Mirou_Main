package com.knj.mirou.boundedContexts.point;

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
    private int currentPoint;
    private int totalGetPoint;
    private int totalUserPoint;
    private Date modifiedAt;
    private Date createdAt;
}
