package com.example.zzbb.search.dto;

import com.example.zzbb.db.dto.DbListResponse;
import com.example.zzbb.qna.dto.QnaListResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private ArrayList<String> hashtags;
    private ArrayList<QnaListResponse> qnas;
    private ArrayList<DbListResponse> dbs;
}
