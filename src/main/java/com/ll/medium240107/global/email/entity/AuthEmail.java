package com.ll.medium240107.global.email.entity;

import com.ll.medium240107.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

import static lombok.AccessLevel.PROTECTED;

@Entity
@SuperBuilder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class AuthEmail extends BaseEntity {
    @Column(unique = true)
    private String authCode;

    @Column(unique = true)
    private String email;

    private Instant expiredDate;

    private boolean isVerified;
}
