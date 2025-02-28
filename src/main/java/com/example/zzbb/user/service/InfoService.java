package com.example.zzbb.user.service;

import com.example.zzbb.user.dto.info.InfoResponse;
import com.example.zzbb.user.entity.User;
import com.example.zzbb.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InfoService {
    private final UserRepository userRepository;

    public InfoResponse getMyInfo(String username) {
        // 1. 유저 정보 가져오기
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return null;
        // 2. DTO에 담기
        InfoResponse response = new InfoResponse(user.getLevel());
        return response;
    }
}
