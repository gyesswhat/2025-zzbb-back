package com.example.zzbb.qna.controller;

import com.example.zzbb.global.ApiResponse;
import com.example.zzbb.jwt.JwtUtil;
import com.example.zzbb.qna.dto.*;
import com.example.zzbb.qna.entity.Comment;
import com.example.zzbb.qna.entity.Likes;
import com.example.zzbb.qna.entity.Scrap;
import com.example.zzbb.qna.service.QnaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
public class QnaController {
    private QnaService qnaService;
    private JwtUtil jwtUtil;

    @GetMapping("/qna/brief")
    public ResponseEntity<ApiResponse<?>> viewBriefQna() {
        ArrayList<BriefQnaResponse> responses = qnaService.viewBriefQna();
        return responses != null ?
                ResponseEntity.ok(ApiResponse.success(responses)):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "데이터가 없습니다."));
    }

    @GetMapping("/qna")
    public ResponseEntity<ApiResponse<?>> viewQnaList() {
        ArrayList<QnaListResponse> responses = qnaService.viewQnaList();
        return responses != null?
                ResponseEntity.ok(ApiResponse.success(responses)):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "데이터가 없습니다."));
    }

    @GetMapping("/qna/{qnaId}")
    public ResponseEntity<ApiResponse<?>> viewQna() {
        QnaResponse response = qnaService.viewQna();
        return response != null?
                ResponseEntity.ok(ApiResponse.success(response)):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "데이터가 없습니다."));
    }

    @PostMapping("/qna/{qnaId}/like")
    public ResponseEntity<ApiResponse<?>> likeQna(@RequestHeader("Authorization") String authorizationHeader,
                                                  @PathVariable("qnaId") Integer qnaId) {
        // 1. 필요한 정보 추출
        String accessToken = jwtUtil.extractToken(authorizationHeader);
        String username = jwtUtil.extractUsername(accessToken);
        // 2. 서비스에서 처리
        Likes likes = qnaService.likeQna(qnaId, username);
        return likes != null?
                ResponseEntity.ok(ApiResponse.created(null)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "좋아요에 실패했습니다."));
    }

    @PostMapping("/qna/{qnaId}/scrap")
    public ResponseEntity<ApiResponse<?>> scrapQna(@RequestHeader("Authorization") String authorizationHeader,
                                                   @PathVariable("qnaId") Integer qnaId) {
        // 1. 필요한 정보 추출
        String accessToken = jwtUtil.extractToken(authorizationHeader);
        String username = jwtUtil.extractUsername(accessToken);
        // 2. 서비스에서 처리
        Scrap scrap = qnaService.scrapQna(qnaId, username);
        return scrap != null?
                ResponseEntity.ok(ApiResponse.created(null)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "스크랩에 실패했습니다."));
    }

    @PostMapping("/qna/{qnaId}/comment")
    public ResponseEntity<ApiResponse<?>> commentQna(@Valid @RequestBody QnaCommentRequest request,
                                                     @RequestHeader("Authorization") String authorizationHeader,
                                                     @PathVariable("qnaId") Integer qnaId) {
        // 1. 필요한 정보 추출
        String accessToken = jwtUtil.extractToken(authorizationHeader);
        String username = jwtUtil.extractUsername(accessToken);
        // 2. 서비스에서 처리
        Comment comment = qnaService.commentQna(qnaId, username, request);
        return comment != null?
                ResponseEntity.ok(ApiResponse.created(null)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "댓글 달기에 실패했습니다."));
    }

    @PostMapping("/qna/post")
    public ResponseEntity<ApiResponse<?>> postQna(@RequestBody QnaPostRequest request,
                                                  @RequestHeader("Authorization") String authorizationHeader) {
        // 1. 필요한 정보 추출
        String accessToken = jwtUtil.extractToken(authorizationHeader);
        String username = jwtUtil.extractUsername(accessToken);
        // 2. 서비스에서 처리
        QnaPostResponse response = qnaService.postQna(username, request);
        return response != null?
                ResponseEntity.ok(ApiResponse.created(response)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "질문 달기에 실패했습니다."));
    }
}
