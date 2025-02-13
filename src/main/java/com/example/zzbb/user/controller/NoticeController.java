package com.example.zzbb.user.controller;

import com.example.zzbb.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    @GetMapping("/user/notice")
    public ResponseEntity<ApiResponse<?>> getMyNotice() {
        return null;
    }
}
