package com.knj.mirou.boundedContext.challenge.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChallenge is a Querydsl query type for Challenge
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallenge extends EntityPathBase<Challenge> {

    private static final long serialVersionUID = 1436217214L;

    public static final QChallenge challenge = new QChallenge("challenge");

    public final com.knj.mirou.base.entity.QBaseEntity _super = new com.knj.mirou.base.entity.QBaseEntity(this);

    public final StringPath contents = createString("contents");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final DatePath<java.time.LocalDate> joinDeadLine = createDate("joinDeadLine", java.time.LocalDate.class);

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath name = createString("name");

    public final EnumPath<com.knj.mirou.boundedContext.challenge.model.enums.ChallengePeriod> period = createEnum("period", com.knj.mirou.boundedContext.challenge.model.enums.ChallengePeriod.class);

    public final NumberPath<Integer> requiredNum = createNumber("requiredNum", Integer.class);

    public final ListPath<com.knj.mirou.boundedContext.reward.model.entity.Reward, com.knj.mirou.boundedContext.reward.model.entity.QReward> reward = this.<com.knj.mirou.boundedContext.reward.model.entity.Reward, com.knj.mirou.boundedContext.reward.model.entity.QReward>createList("reward", com.knj.mirou.boundedContext.reward.model.entity.Reward.class, com.knj.mirou.boundedContext.reward.model.entity.QReward.class, PathInits.DIRECT2);

    public final EnumPath<com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus> status = createEnum("status", com.knj.mirou.boundedContext.challenge.model.enums.ChallengeStatus.class);

    public QChallenge(String variable) {
        super(Challenge.class, forVariable(variable));
    }

    public QChallenge(Path<? extends Challenge> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChallenge(PathMetadata metadata) {
        super(Challenge.class, metadata);
    }

}

