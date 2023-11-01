package com.knj.mirou.boundedContext.coin.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoin is a Querydsl query type for Coin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoin extends EntityPathBase<Coin> {

    private static final long serialVersionUID = -391169163L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoin coin = new QCoin("coin");

    public final com.knj.mirou.base.entity.QBaseEntity _super = new com.knj.mirou.base.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Integer> currentCoin = createNumber("currentCoin", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final com.knj.mirou.boundedContext.member.model.entity.QMember owner;

    public final NumberPath<Integer> totalGetCoin = createNumber("totalGetCoin", Integer.class);

    public final NumberPath<Integer> totalUsedCoin = createNumber("totalUsedCoin", Integer.class);

    public QCoin(String variable) {
        this(Coin.class, forVariable(variable), INITS);
    }

    public QCoin(Path<? extends Coin> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoin(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoin(PathMetadata metadata, PathInits inits) {
        this(Coin.class, metadata, inits);
    }

    public QCoin(Class<? extends Coin> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.owner = inits.isInitialized("owner") ? new com.knj.mirou.boundedContext.member.model.entity.QMember(forProperty("owner"), inits.get("owner")) : null;
    }

}

