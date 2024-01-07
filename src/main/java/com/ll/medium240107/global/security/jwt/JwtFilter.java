package com.ll.medium240107.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ll.medium240107.global.rq.Rq;
import com.ll.medium240107.global.security.jwt.refreshToken.entity.RefreshToken;
import com.ll.medium240107.global.security.jwt.refreshToken.service.RefreshTokenService;
import com.ll.medium240107.global.security.service.UserDetailsServiceImpl;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsServiceImpl userDetailsService;
    private final Rq rq;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtUtils.getJwtFromRequest(request);

        if (token != null) {
            try {
                Jws<Claims> jws = jwtUtils.validateJwtToken(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(jws.getPayload().getSubject());

                rq.setAuthentication(userDetails);
            } catch (JwtException e) {
                if (e instanceof ExpiredJwtException) {
                    String refreshTokenStr = jwtUtils.getRefreshFromRequest(request);

                    RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenStr)
                            .orElseGet(() -> null);

                    if (refreshToken != null && refreshToken.getExpiryDate().isAfter(Instant.now())) {
                        UserDetails userDetails = userDetailsService
                                .loadUserByUsername(refreshToken.getMember().getEmail());

                        rq.setAuthentication(userDetails);

                        String jwt = jwtUtils.generateJwtToken(refreshToken.getMember().getEmail());

                        Cookie jwtCookie = jwtUtils.createJwtCookie(jwt);

                        response.addCookie(jwtCookie);
                    } else {
                        rq.removeJwtCookies();
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                } else {
                    rq.removeJwtCookies();
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}