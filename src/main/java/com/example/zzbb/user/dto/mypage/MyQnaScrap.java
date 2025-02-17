package com.example.zzbb.user.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyQnaScrap {
    private Integer qnaId;
    private String title;
    private String body;
    private Integer comments;
    private Integer likes;
    private Integer scraps;
}
