package com.ll.medium240107.global.security.jwt;

import com.ll.medium240107.domain.member.member.entity.Member;
import com.ll.medium240107.domain.member.member.repository.MemberRepository;
import com.ll.medium240107.global.security.jwt.refreshToken.entity.RefreshToken;
import com.ll.medium240107.global.security.jwt.refreshToken.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@Transactional
public class TestJwt {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    MockMvc mockMvc;

    private final String email = "test@gmail.com";
    private Member member;
    private RefreshToken refreshToken;

    @Test
    @DisplayName("토큰생성 테스트")
    void t1() {
        String token = jwtUtils.generateJwtToken(email);

        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("토큰검증 테스트")
    void t2() {
        String token = jwtUtils.generateJwtToken(email);

        try {
            Jws<Claims> jws = jwtUtils.validateJwtToken(token);
            assertThat(jws.getPayload().getSubject()).isEqualTo(email);
        } catch (JwtException e) {
            assertThat(false).isTrue();
        }
    }

    @Test
    @DisplayName("Jwt 쿠키 전송 테스트")
    void t3() throws Exception {
        member = memberRepository.save(Member.builder()
                .username("test")
                .email(email)
                .password(passwordEncoder.encode("1234"))
                .build());

        String token = jwtUtils.generateJwtToken(email);

        mockMvc.perform(get("/validate").cookie(jwtUtils.createJwtCookie(token)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("jwtFilter 작동 테스트")
    void t4() throws Exception {
        member = memberRepository.save(Member.builder()
                .username("test")
                .email(email)
                .password(passwordEncoder.encode("1234"))
                .build());

        String token = jwtUtils.generateJwtToken(email);

        mockMvc.perform(get("/filterTest").cookie(jwtUtils.createJwtCookie(token)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("jwtFilter 만료 테스트")
    void t5() throws Exception {
        String token = jwtUtils.generateJwtTokenWithMs(email, 1000);

        Thread.sleep(1500);

        mockMvc.perform(get("/filterTest").cookie(jwtUtils.createJwtCookie(token)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("jwttoken 이 만료되어도 refreshtoken이 살아있다면 jwt 재발급")
    void t6() throws Exception {
        member = memberRepository.save(Member.builder()
                .username("test")
                .email(email)
                .password(passwordEncoder.encode("1234"))
                .build());

        refreshToken = refreshTokenService.createByMemberId(member.getId());

        String token = jwtUtils.generateJwtTokenWithMs(email, 1000);

        Thread.sleep(1500);

        mockMvc.perform(
                        get("/filterTest")
                                .cookie(
                                        jwtUtils.createJwtCookie(token),
                                        jwtUtils.createRefreshCookie(refreshToken.getToken())))
                .andExpect(status().isOk());
    }
}