package com.example.zzbb.user.service;

import com.example.zzbb.jwt.JwtUtil;
import com.example.zzbb.jwt.TokenResponse;
import com.example.zzbb.user.dto.auth.JoinRequest;
import com.example.zzbb.user.entity.User;
import com.example.zzbb.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public TokenResponse join(JoinRequest request) {
        if (isUsernameExists(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }

        // 회원가입
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 비밀번호 암호화
        User savedUser = userRepository.save(user);

        // 자동 로그인 처리
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(request.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(request.getUsername());
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);

        return tokenResponse;  // 회원가입한 user 객체 반환
    }

    public boolean isUsernameExists(String username) {
        Long result = userRepository.existsByUsername(username);
        return result != null && result == 1L;  // 1을 true로, 0을 false로 변환
    }
}
