package com.knj.mirou.boundedContexts;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Point {
    @Id

    private long id;
    private int current_point;
    private int total_get_point;
}
