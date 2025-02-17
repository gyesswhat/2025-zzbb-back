package com.example.zzbb.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BriefQnaResponse {
    private Integer qnaId;
    private String title;
    private String image;
    private String generatedTime;
}
