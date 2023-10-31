package com.knj.mirou.boundedContext.coinhistory.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.base.enums.ChangeType;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import jakarta.persistence.*;
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
public class CoinHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member linkedMember;

    private int changedCoin;

    @Enumerated(EnumType.STRING)
    private ChangeType changeType;

    private String contents;
}
