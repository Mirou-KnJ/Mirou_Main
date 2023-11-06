package com.knj.mirou.boundedContext.member.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1098660290L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final com.knj.mirou.base.entity.QBaseEntity _super = new com.knj.mirou.base.entity.QBaseEntity(this);

    public final com.knj.mirou.boundedContext.coin.entity.QCoin coin;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final ListPath<com.knj.mirou.boundedContext.inventory.entity.Inventory, com.knj.mirou.boundedContext.inventory.entity.QInventory> inventory = this.<com.knj.mirou.boundedContext.inventory.entity.Inventory, com.knj.mirou.boundedContext.inventory.entity.QInventory>createList("inventory", com.knj.mirou.boundedContext.inventory.entity.Inventory.class, com.knj.mirou.boundedContext.inventory.entity.QInventory.class, PathInits.DIRECT2);

    public final StringPath inviteCode = createString("inviteCode");

    public final StringPath loginId = createString("loginId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath nickname = createString("nickname");

    public final com.knj.mirou.boundedContext.point.entity.QPoint point;

    public final EnumPath<com.knj.mirou.boundedContext.member.model.enums.MemberRole> role = createEnum("role", com.knj.mirou.boundedContext.member.model.enums.MemberRole.class);

    public final EnumPath<com.knj.mirou.boundedContext.member.model.enums.SocialCode> socialCode = createEnum("socialCode", com.knj.mirou.boundedContext.member.model.enums.SocialCode.class);

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coin = inits.isInitialized("coin") ? new com.knj.mirou.boundedContext.coin.entity.QCoin(forProperty("coin")) : null;
        this.point = inits.isInitialized("point") ? new com.knj.mirou.boundedContext.point.entity.QPoint(forProperty("point")) : null;
    }

}

