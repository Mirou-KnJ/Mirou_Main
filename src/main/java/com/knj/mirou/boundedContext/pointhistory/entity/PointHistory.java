package com.knj.mirou.boundedContext.pointhistory.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.base.enums.ChangeType;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.format.DateTimeFormatter;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class PointHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member linkedMember;

    private int changedPoint;

    @Enumerated(EnumType.STRING)
    private ChangeType changeType;

    private String contents;

    private String imgUrl;

    public String getCreateDateFormattedStr(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 dd일");
        return formatter.format(getCreateDate());
    }

}
