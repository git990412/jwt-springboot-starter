package com.ll.medium240107.domain.member.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.medium240107.domain.member.member.entity.Member;
import com.ll.medium240107.domain.member.member.form.JoinForm;
import com.ll.medium240107.domain.member.member.form.LoginForm;
import com.ll.medium240107.domain.member.member.repository.MemberRepository;
import com.ll.medium240107.global.email.entity.AuthEmail;
import com.ll.medium240107.global.email.repository.EmailRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class TestApiV1MemberController {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EmailRepository mailRepository;

    private final String email = "test@gmail.com";
    private final String password = "12345678";

    @Test
    @DisplayName("회원가입")
    void t0() throws Exception {
        mailRepository.save(AuthEmail.builder()
                .authCode("123456")
                .expiredDate(null)
                .email(email)
                .isVerified(true)
                .build());

        JoinForm joinForm = JoinForm.builder()
                .email(email)
                .username("test")
                .password(password)
                .passwordConfirm(password)
                .build();

        mockMvc.perform(post("/api/v1/member")
                        .content(objectMapper.writeValueAsString(joinForm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 검증 테스트")
    void t1() throws Exception {
        JoinForm joinForm = JoinForm.builder()
                .email(email)
                .username("")
                .password("1234567")
                .passwordConfirm("1234567")
                .build();

        mockMvc.perform(post("/api/v1/member")
                        .content(objectMapper.writeValueAsString(joinForm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인")
    void t2() throws Exception {
        memberRepository.save(Member.builder()
                .email(email)
                .username("test")
                .password(password)
                .build());

        LoginForm loginForm = LoginForm.builder()
                .email(email)
                .password(password)
                .build();

        mockMvc.perform(post("/api/v1/member/login")
                        .content(objectMapper.writeValueAsString(loginForm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 후 isAnonymous 접근 불가 테스트")
    void t3() throws Exception {
        memberRepository.save(Member.builder()
                .email(email)
                .username("test")
                .password(password)
                .build());

        LoginForm loginForm = LoginForm.builder()
                .email(email)
                .password(password)
                .build();

        MvcResult result = mockMvc.perform(post("/api/v1/member/login")
                        .content(objectMapper.writeValueAsString(loginForm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        Cookie[] cookies = response.getCookies();

        mockMvc.perform(post("/api/v1/member/login")
                        .content(objectMapper.writeValueAsString(loginForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookies))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("로그인 후 다른 곳에서 로그인 테스트")
    void t4() throws Exception {
        memberRepository.save(Member.builder()
                .email(email)
                .username("test")
                .password(password)
                .build());

        LoginForm loginForm = LoginForm.builder()
                .email(email)
                .password(password)
                .build();

        mockMvc.perform(post("/api/v1/member/login")
                        .content(objectMapper.writeValueAsString(loginForm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/member/login")
                        .content(objectMapper.writeValueAsString(loginForm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그아웃")
    void t5() throws Exception {
        memberRepository.save(Member.builder()
                .email(email)
                .username("test")
                .password(password)
                .build());

        LoginForm loginForm = LoginForm.builder()
                .email(email)
                .password(password)
                .build();

        MvcResult result = mockMvc.perform(post("/api/v1/member/login")
                        .content(objectMapper.writeValueAsString(loginForm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        Cookie[] cookies = response.getCookies();

        mockMvc.perform(post("/api/v1/member/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookies))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
