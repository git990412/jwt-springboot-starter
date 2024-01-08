package com.ll.medium240107.global.email.controller;

import com.ll.medium240107.global.email.entity.AuthEmail;
import com.ll.medium240107.global.email.service.MailService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class ApiV1EmailController {
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @PostMapping("/sendVerify")
    public ResponseEntity<?> verifyEmail(@RequestParam("email") @Valid @Email String email) {
        String title = "이메일 인증 링크입니다.";
        String uuid = UUID.randomUUID().toString();
        String authCode = passwordEncoder.encode(uuid);

        String authUrl = "http://localhost:8090/api/v1/email/verify?email=" + email + "&authCode=" + uuid;

        mailService.findByEmail(email)
                .ifPresentOrElse(
                        authEmail -> {
                            authEmail.setAuthCode(authCode);
                            authEmail.setExpiredDate(Instant.now().plusSeconds(60 * 5));
                            mailService.updateEmail(authEmail);
                        },
                        () -> mailService.saveEmail(email, authCode));

        mailService.sendEmail(email, title, authUrl);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("email") @Valid @Email String email,
                                         @RequestParam("authCode") String authCode) {
        Optional<AuthEmail> authEmail = mailService.findByEmail(email);

        if (authEmail.isEmpty() ||
                !passwordEncoder.matches(authCode, authEmail.get().getAuthCode()) ||
                Instant.now().isAfter(authEmail.get().getExpiredDate())
        ) {
            return ResponseEntity.badRequest().build();
        }

        mailService.verifyEmail(email);

        return ResponseEntity.ok().build();
    }
}
