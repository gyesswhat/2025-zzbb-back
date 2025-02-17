package com.example.zzbb.user.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyDbScrap {
    private Integer dbId;
    private String title;
    private String body;
    private Integer likes;
    private Integer scraps;

}
