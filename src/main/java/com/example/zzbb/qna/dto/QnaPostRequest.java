package com.example.zzbb.qna.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QnaPostRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String body;
    private ArrayList<String> hashtags;
}
