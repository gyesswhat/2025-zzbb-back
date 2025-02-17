package com.example.zzbb.user.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyBadgeResponse {
    private Integer level;
    private String nickname;
    private Integer questions;
    private Integer answers;
    private Integer reactions;
}
