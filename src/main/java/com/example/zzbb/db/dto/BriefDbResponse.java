package com.example.zzbb.db.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BriefDbResponse {
    private Integer dbId;
    private String title;
    private String generatedTime;
}
