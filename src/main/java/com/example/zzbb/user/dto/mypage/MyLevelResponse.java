package com.example.zzbb.user.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyLevelResponse {
    private Integer currentScore;
    private Integer nextLevelScore;
}
