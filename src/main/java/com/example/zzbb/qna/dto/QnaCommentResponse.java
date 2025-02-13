package com.example.zzbb.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QnaCommentResponse {
    private Integer commentId;
    private String body;
    private Integer userId;
}
