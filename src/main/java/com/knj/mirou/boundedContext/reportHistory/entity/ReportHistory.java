package com.knj.mirou.boundedContext.reportHistory.entity;

import com.knj.mirou.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class ReportHistory extends BaseEntity {

    private long targetFeedId;
    private long reporterId;
    private String contents;

}
