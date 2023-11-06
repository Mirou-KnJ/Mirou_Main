package com.knj.mirou.boundedContext.coinhistory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoinHistory is a Querydsl query type for CoinHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoinHistory extends EntityPathBase<CoinHistory> {

    private static final long serialVersionUID = -986531591L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoinHistory coinHistory = new QCoinHistory("coinHistory");

    public final com.knj.mirou.base.entity.QBaseEntity _super = new com.knj.mirou.base.entity.QBaseEntity(this);

    public final NumberPath<Integer> changedCoin = createNumber("changedCoin", Integer.class);

    public final EnumPath<com.knj.mirou.base.enums.ChangeType> changeType = createEnum("changeType", com.knj.mirou.base.enums.ChangeType.class);

    public final StringPath contents = createString("contents");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final com.knj.mirou.boundedContext.member.model.entity.QMember linkedMember;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public QCoinHistory(String variable) {
        this(CoinHistory.class, forVariable(variable), INITS);
    }

    public QCoinHistory(Path<? extends CoinHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoinHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoinHistory(PathMetadata metadata, PathInits inits) {
        this(CoinHistory.class, metadata, inits);
    }

    public QCoinHistory(Class<? extends CoinHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.linkedMember = inits.isInitialized("linkedMember") ? new com.knj.mirou.boundedContext.member.model.entity.QMember(forProperty("linkedMember"), inits.get("linkedMember")) : null;
    }

}

