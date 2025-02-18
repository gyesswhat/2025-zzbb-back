package com.example.zzbb.user.controller;

import com.example.zzbb.global.ApiResponse;
import com.example.zzbb.jwt.JwtUtil;
import com.example.zzbb.user.dto.notice.NoticeCountResponse;
import com.example.zzbb.user.dto.notice.NoticeReadResponse;
import com.example.zzbb.user.dto.notice.NoticeResponse;
import com.example.zzbb.user.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final JwtUtil jwtUtil;
    private final NoticeService noticeService;

    @GetMapping("/user/notice")
    public ResponseEntity<ApiResponse<?>> getMyNotice(@RequestHeader("Authorization") String authorizationHeader) {
        // 1. 필요한 정보 추출
        String accessToken = jwtUtil.extractToken(authorizationHeader);
        String username = jwtUtil.extractUsername(accessToken);
        // 2. 서비스에서 처리
        ArrayList<NoticeResponse> responses = noticeService.getMyNotice(username);
        return (responses != null)?
                ResponseEntity.ok(ApiResponse.success(responses)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "알림을 불러오는 데 실패했습니다."));
    }

    @GetMapping("/user/notice/count")
    public ResponseEntity<ApiResponse<?>> getMyNoticeCount(@RequestHeader("Authorization") String authorizationHeader) {
        // 1. 필요한 정보 추출
        String accessToken = jwtUtil.extractToken(authorizationHeader);
        String username = jwtUtil.extractUsername(accessToken);
        // 2. 서비스에서 처리
        NoticeCountResponse response = noticeService.getMyNoticeCount(username);
        return (response != null)?
                ResponseEntity.ok(ApiResponse.success(response)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "알림 개수를 불러오는 데 실패했습니다."));
    }

    @PostMapping("/user/notice/{noticeId}/read")
    public ResponseEntity<ApiResponse<?>> readMyNotice(@RequestHeader("Authorization") String authorizationHeader,
                                                       @PathVariable("noticeId") Integer noticeId) {
        // 1. 필요한 정보 추출
        String accessToken = jwtUtil.extractToken(authorizationHeader);
        String username = jwtUtil.extractUsername(accessToken);
        // 2. 서비스에서 처리
        NoticeReadResponse response = noticeService.readMyNotice(username, noticeId);
        return (response != null)?
                ResponseEntity.ok(ApiResponse.success(response)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "알림 읽기에 실패했습니다."));
    }
}
