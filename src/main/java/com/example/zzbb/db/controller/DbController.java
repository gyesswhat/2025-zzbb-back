package com.example.zzbb.db.controller;

import com.example.zzbb.db.service.DbService;
import com.example.zzbb.db.dto.BriefDbResponse;
import com.example.zzbb.db.dto.DbListResponse;
import com.example.zzbb.db.dto.DbResponse;
import com.example.zzbb.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
public class DbController {
    private final DbService dbService;

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
    public ResponseEntity<ApiResponse<?>> viewDb() {
        DbResponse response = dbService.viewDb();
        return response != null?
                ResponseEntity.ok(ApiResponse.success(response)):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "데이터가 없습니다."));
    }
}
