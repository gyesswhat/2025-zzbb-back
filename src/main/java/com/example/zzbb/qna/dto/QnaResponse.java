package com.example.zzbb.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QnaResponse {
    private Integer qnaId;
    private Integer userId;
    private String title;
    private String body;
    private ArrayList<String> keyword;
    private ArrayList<String> imageUrls;
    private Integer numberOfComments;
    private Integer numberOfLikes;
    private Integer numberOfScraps;
    private ArrayList<CommentDto> comments;
}
