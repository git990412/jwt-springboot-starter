package com.ll.medium240107.global.rq;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.ll.medium240107.global.security.jwt.JwtUtils;
import com.ll.medium240107.global.security.service.UserDetailsImpl;

@Component
@RequestScope
@RequiredArgsConstructor
public class Rq {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final JwtUtils jwtUtils;
    private UserDetailsImpl securityUser;

    public void setAuthentication(UserDetails member) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                member,
                member.getPassword(),
                member.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public UserDetailsImpl getSecurityUser() {
        if (securityUser == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null)
                return null;
            Object principal = authentication.getPrincipal();
            if (principal == null)
                return null;
            securityUser = (UserDetailsImpl) principal;
        }

        return securityUser;
    }

    public void removeJwtCookies() {
        response.addCookie(jwtUtils.cleanJwtCookie());
        response.addCookie(jwtUtils.cleanRefreshCookie());
    }

    public void addCookie(Cookie cookie) {
        response.addCookie(cookie);
    }
}