package com.ll.medium240107.global.email.repository;

import com.ll.medium240107.global.email.entity.AuthEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface EmailRepository extends JpaRepository<AuthEmail, Long> {
    void deleteByExpiredDateBefore(Instant expiredDate);

    Optional<AuthEmail> findByEmail(String email);
}
