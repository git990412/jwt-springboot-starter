package com.ll.medium240107.domain.member.member.entity;

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

    private static final long serialVersionUID = -2043829207L;

    public static final QMember member = new QMember("member1");

    public final com.ll.medium240107.global.jpa.entity.QBaseEntity _super = new com.ll.medium240107.global.jpa.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath email = createString("email");

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath password = createString("password");

    public final SetPath<com.ll.medium240107.domain.member.role.entity.Role, com.ll.medium240107.domain.member.role.entity.QRole> roles = this.<com.ll.medium240107.domain.member.role.entity.Role, com.ll.medium240107.domain.member.role.entity.QRole>createSet("roles", com.ll.medium240107.domain.member.role.entity.Role.class, com.ll.medium240107.domain.member.role.entity.QRole.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

