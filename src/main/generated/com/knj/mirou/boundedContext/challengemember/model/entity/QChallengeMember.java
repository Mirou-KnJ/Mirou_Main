package com.knj.mirou.boundedContext.challengemember.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChallengeMember is a Querydsl query type for ChallengeMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallengeMember extends EntityPathBase<ChallengeMember> {

    private static final long serialVersionUID = 1190568222L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChallengeMember challengeMember = new QChallengeMember("challengeMember");

    public final com.knj.mirou.base.entity.QBaseEntity _super = new com.knj.mirou.base.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final com.knj.mirou.boundedContext.challenge.model.entity.QChallenge linkedChallenge;

    public final com.knj.mirou.boundedContext.member.model.entity.QMember linkedMember;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final EnumPath<com.knj.mirou.boundedContext.challengemember.model.enums.Progress> progress = createEnum("progress", com.knj.mirou.boundedContext.challengemember.model.enums.Progress.class);

    public final NumberPath<Integer> successNumber = createNumber("successNumber", Integer.class);

    public QChallengeMember(String variable) {
        this(ChallengeMember.class, forVariable(variable), INITS);
    }

    public QChallengeMember(Path<? extends ChallengeMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChallengeMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChallengeMember(PathMetadata metadata, PathInits inits) {
        this(ChallengeMember.class, metadata, inits);
    }

    public QChallengeMember(Class<? extends ChallengeMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.linkedChallenge = inits.isInitialized("linkedChallenge") ? new com.knj.mirou.boundedContext.challenge.model.entity.QChallenge(forProperty("linkedChallenge")) : null;
        this.linkedMember = inits.isInitialized("linkedMember") ? new com.knj.mirou.boundedContext.member.model.entity.QMember(forProperty("linkedMember"), inits.get("linkedMember")) : null;
    }

}

