package com.example.zzbb.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DbResponse {
    private Integer dbId;
    private String title;
    private String body;
    private ArrayList<String> keyword;
    private ArrayList<String> imageUrls;
    private Integer numberOfLikes;
    private Integer numberOfScraps;
    private Boolean isLiked; // is는 함수로 인식함
    private Boolean isScraped;
}
