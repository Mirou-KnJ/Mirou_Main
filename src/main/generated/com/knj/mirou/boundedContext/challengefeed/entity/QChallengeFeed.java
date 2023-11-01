package com.knj.mirou.boundedContext.challengefeed.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChallengeFeed is a Querydsl query type for ChallengeFeed
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallengeFeed extends EntityPathBase<ChallengeFeed> {

    private static final long serialVersionUID = 1389127929L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChallengeFeed challengeFeed = new QChallengeFeed("challengeFeed");

    public final com.knj.mirou.base.entity.QBaseEntity _super = new com.knj.mirou.base.entity.QBaseEntity(this);

    public final StringPath contents = createString("contents");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final com.knj.mirou.boundedContext.challenge.model.entity.QChallenge linkedChallenge;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final com.knj.mirou.boundedContext.member.model.entity.QMember writer;

    public QChallengeFeed(String variable) {
        this(ChallengeFeed.class, forVariable(variable), INITS);
    }

    public QChallengeFeed(Path<? extends ChallengeFeed> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChallengeFeed(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChallengeFeed(PathMetadata metadata, PathInits inits) {
        this(ChallengeFeed.class, metadata, inits);
    }

    public QChallengeFeed(Class<? extends ChallengeFeed> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.linkedChallenge = inits.isInitialized("linkedChallenge") ? new com.knj.mirou.boundedContext.challenge.model.entity.QChallenge(forProperty("linkedChallenge")) : null;
        this.writer = inits.isInitialized("writer") ? new com.knj.mirou.boundedContext.member.model.entity.QMember(forProperty("writer"), inits.get("writer")) : null;
    }

}

