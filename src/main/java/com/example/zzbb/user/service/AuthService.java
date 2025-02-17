package com.example.zzbb.user.service;

import com.example.zzbb.jwt.JwtUtil;
import com.example.zzbb.jwt.TokenResponse;
import com.example.zzbb.user.dto.auth.JoinRequest;
import com.example.zzbb.user.entity.RefreshToken;
import com.example.zzbb.user.entity.User;
import com.example.zzbb.user.repository.RefreshTokenRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenResponse join(JoinRequest request) {
        if (isUsernameExists(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }

        // 회원가입
        User user = new User(
                null,
                request.getIsNewbie(),
                request.getUsername(),
                request.getNickname(),
                passwordEncoder.encode(request.getPassword()),
                1,
                0,
                null
        );
        User savedUser = userRepository.save(user);

        // 자동 로그인 처리
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(request.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(request.getUsername());
        RefreshToken refreshTokenEntity = new RefreshToken(null, user, refreshToken);
        user.setRefreshToken(refreshTokenEntity);
        refreshTokenRepository.save(refreshTokenEntity);
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);

        return tokenResponse;  // 회원가입한 user 객체 반환
    }

    public boolean isUsernameExists(String username) {
        Long result = userRepository.existsByUsername(username);
        return result != null && result == 1L;  // 1을 true로, 0을 false로 변환
    }
}
