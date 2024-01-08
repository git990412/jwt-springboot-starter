package com.ll.medium240107.global.email.service;

import com.ll.medium240107.global.email.entity.AuthEmail;
import com.ll.medium240107.global.email.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;
    private final EmailRepository emailRepository;

    public void sendEmail(String toEmail,
                          String title,
                          String text) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);
            throw new RuntimeException(e);
        }
    }

    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createEmailForm(String toEmail,
                                              String title,
                                              String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }

    public void updateEmail(AuthEmail authEmail) {
        emailRepository.save(authEmail);
    }

    public void saveEmail(String toEmail,
                          String authCode) {
        emailRepository.save(AuthEmail.builder()
                .email(toEmail)
                .authCode(authCode)
                .isVerified(false)
                .expiredDate(Instant.now().plus(5, ChronoUnit.MINUTES))
                .build());
    }

    public void verifyEmail(String email) {
        AuthEmail authEmail = emailRepository.findByEmail(email).get();
        authEmail.setVerified(true);
    }

    public Optional<AuthEmail> findByEmail(String email) {
        return emailRepository.findByEmail(email);
    }
}