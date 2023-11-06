package com.knj.mirou.boundedContext.reward.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReward is a Querydsl query type for Reward
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReward extends EntityPathBase<Reward> {

    private static final long serialVersionUID = 93496940L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReward reward1 = new QReward("reward1");

    public final com.knj.mirou.base.entity.QBaseEntity _super = new com.knj.mirou.base.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final com.knj.mirou.boundedContext.challenge.model.entity.QChallenge linkedChallenge;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath reward = createString("reward");

    public final EnumPath<com.knj.mirou.boundedContext.reward.model.enums.RewardType> rewardType = createEnum("rewardType", com.knj.mirou.boundedContext.reward.model.enums.RewardType.class);

    public final NumberPath<Integer> round = createNumber("round", Integer.class);

    public QReward(String variable) {
        this(Reward.class, forVariable(variable), INITS);
    }

    public QReward(Path<? extends Reward> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReward(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReward(PathMetadata metadata, PathInits inits) {
        this(Reward.class, metadata, inits);
    }

    public QReward(Class<? extends Reward> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.linkedChallenge = inits.isInitialized("linkedChallenge") ? new com.knj.mirou.boundedContext.challenge.model.entity.QChallenge(forProperty("linkedChallenge")) : null;
    }

}

