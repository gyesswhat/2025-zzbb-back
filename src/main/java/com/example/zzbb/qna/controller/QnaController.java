package com.example.zzbb.qna.controller;

import com.example.zzbb.global.ApiResponse;
import com.example.zzbb.jwt.JwtUtil;
import com.example.zzbb.qna.dto.*;
import com.example.zzbb.qna.entity.QnaComment;
import com.example.zzbb.qna.service.QnaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class QnaController {
    private final QnaService qnaService;
    private final JwtUtil jwtUtil;

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
    public ResponseEntity<ApiResponse<?>> viewQna(@PathVariable("qnaId") Integer qnaId) {
        QnaResponse response = qnaService.viewQna(qnaId);
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
        QnaLikeResponse response = qnaService.likeQna(qnaId, username);
        return response != null?
                ResponseEntity.ok(ApiResponse.success(response)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "좋아요에 실패했습니다."));
    }

    @PostMapping("/qna/{qnaId}/scrap")
    public ResponseEntity<ApiResponse<?>> scrapQna(@RequestHeader("Authorization") String authorizationHeader,
                                                   @PathVariable("qnaId") Integer qnaId) {
        // 1. 필요한 정보 추출
        String accessToken = jwtUtil.extractToken(authorizationHeader);
        String username = jwtUtil.extractUsername(accessToken);
        // 2. 서비스에서 처리
        QnaScrapResponse response = qnaService.scrapQna(qnaId, username);
        return response != null?
                ResponseEntity.ok(ApiResponse.success(response)):
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
        QnaComment qnaComment = qnaService.commentQna(qnaId, username, request);
        return qnaComment != null?
                ResponseEntity.ok(ApiResponse.created(null)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "댓글 달기에 실패했습니다."));
    }

    @PostMapping(value = "/qna/post", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<?>> postQna(@Valid @RequestPart(name = "data") QnaPostRequest request,
                                                  @RequestPart(name = "file", required = false) List<MultipartFile> multipartFilelist,
                                                  @RequestHeader("Authorization") String authorizationHeader) throws IOException {
        // 1. 필요한 정보 추출
        String accessToken = jwtUtil.extractToken(authorizationHeader);
        String username = jwtUtil.extractUsername(accessToken);
        // 2. 서비스에서 처리
        QnaPostResponse response = qnaService.postQna(username, request, multipartFilelist);
        // 3. 사진 등록
        return  (response != null)?
                ResponseEntity.ok(ApiResponse.created(response)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "질문 달기에 실패했습니다."));
    }
}
