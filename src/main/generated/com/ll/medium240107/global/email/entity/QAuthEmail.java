package com.ll.medium240107.global.email.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuthEmail is a Querydsl query type for AuthEmail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthEmail extends EntityPathBase<AuthEmail> {

    private static final long serialVersionUID = -187700296L;

    public static final QAuthEmail authEmail = new QAuthEmail("authEmail");

    public final com.ll.medium240107.global.jpa.entity.QBaseEntity _super = new com.ll.medium240107.global.jpa.entity.QBaseEntity(this);

    public final StringPath authCode = createString("authCode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.Instant> expiredDate = createDateTime("expiredDate", java.time.Instant.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath isVerified = createBoolean("isVerified");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public QAuthEmail(String variable) {
        super(AuthEmail.class, forVariable(variable));
    }

    public QAuthEmail(Path<? extends AuthEmail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuthEmail(PathMetadata metadata) {
        super(AuthEmail.class, metadata);
    }

}

