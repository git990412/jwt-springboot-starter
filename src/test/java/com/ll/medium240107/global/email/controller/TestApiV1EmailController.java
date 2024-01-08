package com.ll.medium240107.global.email.controller;

import com.ll.medium240107.global.email.repository.EmailRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class TestApiV1EmailController {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    EmailRepository mailRepository;

    @Value("${spring.mail.username}")
    String mailAddress;

    @Test
    @DisplayName("이메일 인증 링크 발송 테스트")
    void t1() throws Exception {
        mockMvc.perform(post("/api/v1/email/sendVerify")
                        .param("email", mailAddress))
                .andExpect(status().isOk());
    }
}
