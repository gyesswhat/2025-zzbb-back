package com.example.zzbb.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final UserDetails principal;

    // JWT 인증 성공 후 사용할 생성자
    public JwtAuthenticationToken(UserDetails principal) {
        super(principal.getAuthorities()); // 권한 정보 저장
        this.principal = principal;
        setAuthenticated(true); // 이미 인증된 상태로 설정
    }

    @Override
    public Object getCredentials() {
        return null; // JWT 기반 인증이므로 비밀번호 필요 없음
    }

    @Override
    public Object getPrincipal() {
        return principal; // 사용자 정보(UserDetails) 반환
    }
}
