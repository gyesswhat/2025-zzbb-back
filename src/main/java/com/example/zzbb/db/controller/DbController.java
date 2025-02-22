package com.example.zzbb.db.controller;

import com.example.zzbb.db.dto.*;
import com.example.zzbb.db.service.DbService;
import com.example.zzbb.global.ApiResponse;
import com.example.zzbb.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
public class DbController {
    private final DbService dbService;
    private final JwtUtil jwtUtil;

    @GetMapping("/db/brief")
    public ResponseEntity<ApiResponse<?>> viewBriefDb() {
        ArrayList<BriefDbResponse> responses = dbService.viewBriefDb();
        return responses != null?
                ResponseEntity.ok(ApiResponse.success(responses)):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "데이터가 없습니다."));
    }

    @GetMapping("/db")
    public ResponseEntity<ApiResponse<?>> viewDbList() {
        ArrayList<DbListResponse> responses = dbService.viewDbList();
        return responses != null?
                ResponseEntity.ok(ApiResponse.success(responses)):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "데이터가 없습니다."));
    }

    @GetMapping("/db/{dbId}")
    public ResponseEntity<ApiResponse<?>> viewDb(@PathVariable("dbId") Integer dbId) {
        DbResponse response = dbService.viewDb(dbId);
        return response != null?
                ResponseEntity.ok(ApiResponse.success(response)):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "데이터가 없습니다."));
    }

    @PostMapping("/db/{dbId}/like")
    public ResponseEntity<ApiResponse<?>> likeDb(@RequestHeader("Authorization") String authorizationHeader,
                                                  @PathVariable("dbId") Integer qnaId) {
        // 1. 필요한 정보 추출
        String accessToken = jwtUtil.extractToken(authorizationHeader);
        String username = jwtUtil.extractUsername(accessToken);
        // 2. 서비스에서 처리
        DbLikeResponse response = dbService.likeDb(qnaId, username);
        return response != null?
                ResponseEntity.ok(ApiResponse.success(response)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "좋아요에 실패했습니다."));
    }

    @PostMapping("/db/{dbId}/scrap")
    public ResponseEntity<ApiResponse<?>> scrapDb(@RequestHeader("Authorization") String authorizationHeader,
                                                   @PathVariable("dbId") Integer qnaId) {
        // 1. 필요한 정보 추출
        String accessToken = jwtUtil.extractToken(authorizationHeader);
        String username = jwtUtil.extractUsername(accessToken);
        // 2. 서비스에서 처리
        DbScrapResponse response = dbService.scrapDb(qnaId, username);
        return response != null?
                ResponseEntity.ok(ApiResponse.success(response)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "스크랩에 실패했습니다."));
    }
}
