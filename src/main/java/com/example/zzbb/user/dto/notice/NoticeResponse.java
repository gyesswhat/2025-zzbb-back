package com.example.zzbb.user.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponse {
    private Integer noticeId;
    private Integer qnaId;
    private Boolean isScraped;
    private String title;
    private String commentBody;
    private String createdTime;
    private Boolean isRead;
}
