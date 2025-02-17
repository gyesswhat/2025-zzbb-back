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
public class MyScrapResponse {
    private ArrayList<MyQnaScrap> myQnaScrap;
    private ArrayList<MyDbScrap> myDbScrap;
}
