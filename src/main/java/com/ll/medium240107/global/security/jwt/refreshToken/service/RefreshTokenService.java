package com.ll.medium240107.global.security.jwt.refreshToken.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.medium240107.domain.member.member.repository.MemberRepository;
import com.ll.medium240107.global.security.jwt.refreshToken.entity.RefreshToken;
import com.ll.medium240107.global.security.jwt.refreshToken.repository.RefreshTokenRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    @Value("${jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Transactional
    public RefreshToken createByMemberId(Long id) {
        return refreshTokenRepository.save(RefreshToken.builder()
                .member(memberRepository.findById(id).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build());
    }

    public Optional<RefreshToken> findByMemberId(Long id) {
        return refreshTokenRepository.findByMemberId(id);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public int deleteByMemberId(Long id) {
        return refreshTokenRepository.deleteByMember(memberRepository.findById(id).get());
    }
}