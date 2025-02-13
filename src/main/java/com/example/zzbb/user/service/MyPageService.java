package com.example.zzbb.user.service;

import com.example.zzbb.user.dto.mypage.MyHistoryResponse;
import com.example.zzbb.user.dto.mypage.MyQnaResponse;
import com.example.zzbb.user.dto.mypage.MyScrapResponse;
import com.example.zzbb.user.dto.mypage.MyStatisticsResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyPageService {
    public MyStatisticsResponse getMyStatistics(String username) {
        return null;
    }

    public MyHistoryResponse getMyHistory(String username) {
        return null;
    }

    public ArrayList<MyQnaResponse> getMyQna(String username) {
        return null;
    }

    public ArrayList<MyScrapResponse> getMyScrap(String username) {
        return null;
    }
}
