package com.ll.medium240107.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${jwt.jwtCookieName}")
    private String jwtCookieName;

    @Value("${jwt.refreshCookieName}")
    private String refreshCookieName;

    private final SecretKey secret;

    public JwtUtils() {
        secret = Jwts.SIG.HS512.key().build();
    }

    public String generateJwtToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(secret)
                .compact();
    }

    public String generateJwtTokenWithMs(String email, int ms) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + ms))
                .signWith(secret)
                .compact();
    }

    public Jws<Claims> validateJwtToken(String authToken) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(authToken);
    }

    public Cookie createJwtCookie(String token) {
        Cookie cookie = new Cookie(jwtCookieName, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 14);
        cookie.setPath("/api");
        return cookie;
    }

    public Cookie createRefreshCookie(String token) {
        Cookie cookie = new Cookie(refreshCookieName, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 14);
        cookie.setPath("/api");
        return cookie;
    }

    public Cookie cleanJwtCookie() {
        Cookie cookie = new Cookie(jwtCookieName, "");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/api");
        return cookie;
    }

    public Cookie cleanRefreshCookie() {
        Cookie cookie = new Cookie(refreshCookieName, "");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/api");
        return cookie;
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);

        if (cookie != null)
            return cookie.getValue();
        else
            return null;
    }

    public String getRefreshFromRequest(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, refreshCookieName);

        if (cookie != null)
            return cookie.getValue();
        else
            return null;
    }
}