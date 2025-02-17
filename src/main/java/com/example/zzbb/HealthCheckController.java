package com.example.zzbb;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
