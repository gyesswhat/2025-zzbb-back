package com.example.zzbb.jwt;

import com.example.zzbb.user.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/") ||
                requestURI.startsWith("/user/login") ||
                requestURI.startsWith("/user/join") ||
                requestURI.startsWith("/user/join/verify") ||
                requestURI.startsWith("/user/logout") ||
                requestURI.startsWith("/user/find-password")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 1. Authorization 헤더 확인
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new AccessDeniedException("토큰을 작성해주세요.");
            }

            // 2. 토큰 추출 및 검증
            String token = authHeader.substring(7);

            if (tokenBlacklistService.isBlacklisted(token)) {
                throw new AccessDeniedException("로그아웃된 사용자입니다.");
            }

            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // 3. 인증 객체 생성 및 SecurityContext에 저장
                var auth = new JwtAuthenticationToken(userDetails);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            else {
                throw new AccessDeniedException("토큰 값이 잘못되었습니다.");
            }

            // 4. 다음 필터로 넘어가기
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException e) {
            // 403 상태 코드와 메시지 반환
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write("{\"status\": 403, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
