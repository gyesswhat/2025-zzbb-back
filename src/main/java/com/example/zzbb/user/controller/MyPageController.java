package com.example.zzbb.user.controller;

import com.example.zzbb.global.ApiResponse;
import com.example.zzbb.jwt.JwtUtil;
import com.example.zzbb.user.dto.mypage.*;
import com.example.zzbb.user.service.AuthService;
import com.example.zzbb.user.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @GetMapping("/user/my-page/history")
    public ResponseEntity<ApiResponse<?>> getMyHistory(@RequestHeader("Authorization") String authorizationHeader) {
        // 1. 필요한 정보 추출
        String accessToken = jwtUtil.extractToken(authorizationHeader);
        String username = jwtUtil.extractUsername(accessToken);
        // 2. 서비스에서 처리
        MyHistoryResponse response = myPageService.getMyHistory(username);
        return (response != null)?
                ResponseEntity.ok(ApiResponse.success(response)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "커뮤니티 정보를 불러올 수 없습니다."));
    }

    @GetMapping("/user/my-page/history/qna")
    public ResponseEntity<ApiResponse<?>> getMyQna(@RequestHeader("Authorization") String authorizationHeader) {
        // 1. 필요한 정보 추출
        String accessToken = jwtUtil.extractToken(authorizationHeader);
        String username = jwtUtil.extractUsername(accessToken);
        // 2. 서비스에서 처리
        ArrayList<MyQnaResponse> responses = myPageService.getMyQna(username);
        return (responses != null)?
                ResponseEntity.ok(ApiResponse.success(responses)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "글 불러오기에 실패했습니다."));
    }

    @GetMapping("/user/my-page/history/scrap")
    public ResponseEntity<ApiResponse<?>> getMyScrap(@RequestHeader("Authorization") String authorizationHeader) {
        // 1. 필요한 정보 추출
        String accessToken = jwtUtil.extractToken(authorizationHeader);
        String username = jwtUtil.extractUsername(accessToken);
        // 2. 서비스에서 처리
        MyScrapResponse response = myPageService.getMyScrap(username);
        return (response != null)?
                ResponseEntity.ok(ApiResponse.success(response)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "글 불러오기에 실패했습니다."));
    }

    @GetMapping("/user/my-page/badge")
    public ResponseEntity<ApiResponse<?>> getMyBadge(@RequestHeader("Authorization") String authorizationHeader) {
        // 1. 필요한 정보 추출
        String accessToken = jwtUtil.extractToken(authorizationHeader);
        String username = jwtUtil.extractUsername(accessToken);
        // 2. 서비스에서 처리
        MyBadgeResponse response = myPageService.getMyBadge(username);
        return (response != null)?
                ResponseEntity.ok(ApiResponse.created(response)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "뱃지 정보 불러오기에 실패했습니다."));
    }

//    @PostMapping("/user/my-page/quit")
//    public ResponseEntity<ApiResponse<?>> quit(@RequestHeader("Authorization") String authorizationHeader) {
//        // 1. 필요한 정보 추출
//        String accessToken = jwtUtil.extractToken(authorizationHeader);
//        String username = jwtUtil.extractUsername(accessToken);
//        // 2. 서비스에서 처리
//        authService.quit(username);
//        if (authService.isQuited(username))
//            return ResponseEntity.ok(ApiResponse.success(null));
//        else
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "탈퇴에 실패했습니다."));
//    }
}
