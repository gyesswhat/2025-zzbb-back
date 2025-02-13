package com.example.zzbb.qna.service;

import com.example.zzbb.qna.dto.*;
import com.example.zzbb.qna.entity.Comment;
import com.example.zzbb.qna.entity.Likes;
import com.example.zzbb.qna.entity.Scrap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class QnaService {
    public ArrayList<BriefQnaResponse> viewBriefQna() {
        return null;
    }

    public ArrayList<QnaListResponse> viewQnaList() {
        return null;
    }

    public QnaResponse viewQna() {
        return null;
    }

    public Likes likeQna(Integer qnaId, String username) {
        return null;
    }

    public Scrap scrapQna(Integer qnaId, String username) {
        return null;
    }

    public Comment commentQna(Integer qnaId, String username, QnaCommentRequest request) {
        return null;
    }

    public QnaPostResponse postQna(String username, QnaPostRequest request) {
        return null;
    }
}
