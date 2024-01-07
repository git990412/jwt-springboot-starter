package com.ll.medium240107.global.security.jwt.refreshToken.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import com.ll.medium240107.domain.member.member.entity.Member;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    private Member member;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;
}