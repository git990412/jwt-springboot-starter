package com.ll.medium240107.domain.member.member.controller;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.medium240107.domain.member.member.entity.Member;
import com.ll.medium240107.domain.member.member.form.JoinForm;
import com.ll.medium240107.domain.member.member.form.LoginForm;
import com.ll.medium240107.domain.member.member.service.MemberService;
import com.ll.medium240107.global.rq.Rq;
import com.ll.medium240107.global.security.jwt.JwtUtils;
import com.ll.medium240107.global.security.jwt.refreshToken.entity.RefreshToken;
import com.ll.medium240107.global.security.jwt.refreshToken.service.RefreshTokenService;
import com.ll.medium240107.global.security.service.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class ApiV1MemberController {
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @PostMapping("")
    public ResponseEntity<?> join(@Valid @RequestBody JoinForm joinForm) {
        if (joinForm.getPassword().equals(joinForm.getPasswordConfirm())) {
            memberService.join(joinForm);

            return ResponseEntity.ok().build();
        } else {
            HashMap<String, String> error = new HashMap<>();
            error.put("passwordConfirm", "비밀번호가 일치하지 않습니다.");

            return ResponseEntity.badRequest().body(error);
        }
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginForm loginForm) {
        HashMap<String, String> error = new HashMap<>();
        error.put("password", "아이디 또는 비밀번호가 일치하지 않습니다.");

        Optional<Member> optionalMember = memberService.findByEmail(loginForm.getEmail());

        if (!optionalMember.isPresent()) {
            return ResponseEntity.badRequest().body(error);
        }

        Member member = optionalMember.get();

        RefreshToken refreshToken = refreshTokenService.findByMemberId(member.getId())
                .orElseGet(() -> refreshTokenService.createByMemberId(member.getId()));
        String jwtToken = jwtUtils.generateJwtToken(member.getEmail());

        rq.addCookie(jwtUtils.createJwtCookie(jwtToken));
        rq.addCookie(jwtUtils.createRefreshCookie(refreshToken.getToken()));

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        UserDetailsImpl securityUser = rq.getSecurityUser();

        if (securityUser == null)
            return ResponseEntity.badRequest().build();

        refreshTokenService.deleteByMemberId(securityUser.getId());

        rq.removeJwtCookies();

        return ResponseEntity.ok().build();
    }
}
