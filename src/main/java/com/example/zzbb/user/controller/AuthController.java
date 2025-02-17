package com.example.zzbb.user.controller;

import com.example.zzbb.global.ApiResponse;
import com.example.zzbb.jwt.JwtUtil;
import com.example.zzbb.jwt.TokenResponse;
import com.example.zzbb.user.dto.auth.*;
import com.example.zzbb.user.entity.RefreshToken;
import com.example.zzbb.user.entity.User;
import com.example.zzbb.user.repository.RefreshTokenRepository;
import com.example.zzbb.user.repository.UserRepository;
import com.example.zzbb.user.service.AuthService;
import com.example.zzbb.user.service.TokenBlacklistService;
import com.univcert.api.UnivCert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistService tokenBlacklistService;

    @Value("${univcert.api.key}")
    private String UNIVCERT_API_KEY;

    @PostMapping("/user/login")
    @Transactional
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException(""));

        refreshTokenRepository.deleteByUser(user);

        String accessToken = jwtUtil.generateAccessToken(request.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(request.getUsername());

        refreshTokenRepository.save(new RefreshToken(null, user, refreshToken));

        return ResponseEntity.ok(ApiResponse.success(new TokenResponse(accessToken, refreshToken)));
    }

    @PostMapping("/user/refresh")
    public ResponseEntity<ApiResponse<?>> refresh(@RequestBody RefreshRequest request) {
        if (jwtUtil.validateToken(request.getRefreshToken())) {
            String username = jwtUtil.extractUsername(request.getRefreshToken());
            String newAccessToken = jwtUtil.generateAccessToken(username);
            return ResponseEntity.ok(ApiResponse.success(new RefreshResponse(newAccessToken)));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(403, "Invalid refresh token"));
    }

    @PostMapping("/user/join")
    public ResponseEntity<ApiResponse<?>> join(@Valid @RequestBody JoinRequest request) {
        TokenResponse tokenResponse = authService.join(request);
        if (tokenResponse == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "회원가입에 실패했습니다."));
        return ResponseEntity.ok(ApiResponse.success(tokenResponse));
    }

    @PostMapping("/user/join/verify/send")
    public ResponseEntity<ApiResponse<?>> sendEmail(@RequestBody EmailSendRequest request) throws IOException {
        Map<String, Object> univcertResponse = UnivCert.certify(UNIVCERT_API_KEY, request.getEmail(), "이화여자대학교", true);

        if (univcertResponse != null && univcertResponse.get("success") != null && (boolean) univcertResponse.get("success"))
            return ResponseEntity.ok(ApiResponse.success(null));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "이메일 전송에 실패했습니다."));
    }

    @PostMapping("/user/join/verify/check")
    public ResponseEntity<ApiResponse<?>> checkEmail(@RequestBody EmailVerifyRequest request) throws IOException {
        Map<String, Object> univcertResponse = UnivCert.certifyCode(UNIVCERT_API_KEY, request.getEmail(), "이화여자대학교", request.getCode());

        if (univcertResponse != null && univcertResponse.get("success") != null && (boolean) univcertResponse.get("success"))
            return ResponseEntity.ok(ApiResponse.success(null));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(400, "코드가 일치하지 않습니다."));
    }

    @PostMapping("/user/logout")
    @Transactional
    public ResponseEntity<ApiResponse<?>> logout(@RequestHeader("Authorization") String authorizationHeader) {
        // 1. 필요한 정보 추출
        String accessToken = jwtUtil.extractToken(authorizationHeader);
        String username = jwtUtil.extractUsername(accessToken);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "토큰이 유효하지 않습니다."));
        }

        // Redis에 블랙리스트로 추가하여 무효화
        tokenBlacklistService.addToBlacklist(accessToken);

        // 액세스 토큰에서 userId 추출
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 해당 사용자의 리프레시 토큰을 데이터베이스에서 삭제하여 무효화
        refreshTokenRepository.deleteByUser(user);
        return ResponseEntity.ok(ApiResponse.success("로그아웃이 완료되었습니다."));
    }
}
