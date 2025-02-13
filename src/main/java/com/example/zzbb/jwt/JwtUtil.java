package com.example.zzbb.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;
    private final long accessTokenValidity = 1000 * 60 * 60; // 60분
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7; // 7일

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // ✅ Access Token 생성
    public String generateAccessToken(String username) {
        return generateToken(username, accessTokenValidity);
    }

    // ✅ Refresh Token 생성
    public String generateRefreshToken(String username) {
        return generateToken(username, refreshTokenValidity);
    }

    private String generateToken(String username, long validity) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ 토큰에서 사용자명 추출
    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // ✅ 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ✅ 액세스 토큰 재발급
    // ✅ 토큰의 유효기간 확인

    // ✅ 헤더에서 토큰 추출
    public String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

}
