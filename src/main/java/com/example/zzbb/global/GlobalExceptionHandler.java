package com.example.zzbb.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 400 Bad Request (잘못된 요청)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Void> handleBadRequest(IllegalArgumentException e) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    // 401 Unauthorized (인증 필요)
    @ExceptionHandler(SecurityException.class)
    public ApiResponse<Void> handleUnauthorized(SecurityException e) {
        return ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "로그인이 필요합니다.");
    }

    // 403 Forbidden (접근 권한 없음)
    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<Void> handleForbidden(AccessDeniedException e) {
        return ApiResponse.error(HttpStatus.FORBIDDEN.value(), "접근 권한이 없습니다.");
    }

    // 500 Internal Server Error (서버 내부 오류)
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleGeneralException(Exception e) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다.");
    }

    // 400 Bad Request - @Valid 검증 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "입력값이 유효하지 않습니다.");
    }
}