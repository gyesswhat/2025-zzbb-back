package com.example.zzbb.search.controller;

import com.example.zzbb.global.ApiResponse;
import com.example.zzbb.search.dto.SearchRequest;
import com.example.zzbb.search.dto.SearchResponse;
import com.example.zzbb.search.service.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<?>> search(@Valid @RequestBody SearchRequest request) {
        SearchResponse response = searchService.search(request);
        return (response != null)?
                ResponseEntity.ok(ApiResponse.success(response)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, null));
    }
}
