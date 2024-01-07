package com.ll.medium240107.global.security.jwt.refreshToken.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.medium240107.domain.member.member.entity.Member;
import com.ll.medium240107.global.security.jwt.refreshToken.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String refreshToken);

    int deleteByMember(Member member);

    Optional<RefreshToken> findByMemberId(Long memberId);
}