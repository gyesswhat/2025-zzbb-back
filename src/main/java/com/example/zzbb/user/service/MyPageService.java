package com.example.zzbb.user.service;

import com.example.zzbb.db.repository.DbRepository;
import com.example.zzbb.qna.entity.*;
import com.example.zzbb.qna.repository.CommentRepository;
import com.example.zzbb.qna.repository.LikeRepository;
import com.example.zzbb.qna.repository.QnaRepository;
import com.example.zzbb.qna.repository.ScrapRepository;
import com.example.zzbb.user.dto.mypage.*;
import com.example.zzbb.user.entity.User;
import com.example.zzbb.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final QnaRepository qnaRepository;
    private final DbRepository dbRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

    public MyStatisticsResponse getMyStatistics(String username) {
        // 1. 유저 정보 가져오기
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return  null;

        // 2. qna, db, reactions 정보 가져오기
        ArrayList<Qna> questions = qnaRepository.findByUser(user);
        ArrayList<QnaComment> qnaComments = commentRepository.findByUser(user);
        ArrayList<QnaLike> qnaLikes = likeRepository.findByUser(user);
        ArrayList<QnaScrap> qnaScraps = scrapRepository.findByUser(user);

        // 3. response에 담기
        MyStatisticsResponse response = new MyStatisticsResponse(
                questions.size(),
                qnaComments.size(),
                qnaLikes.size()+ qnaScraps.size()
        );
        // 4. 반환
        return response;
    }

    public MyHistoryResponse getMyHistory(String username) {
        // 1. 유저 정보 가져오기
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return  null;

        // 2. 작성글, 스크랩글 정보 받아오기
        ArrayList<Qna> wroteArticles = qnaRepository.findByUser(user);
        ArrayList<QnaScrap> scrapedArticles = scrapRepository.findByUser(user);

        // 3. response에 담기
        MyHistoryResponse response = new MyHistoryResponse(
                wroteArticles.size(),
                scrapedArticles.size()
        );
        // 4. 반환
        return response;
    }

    public ArrayList<MyQnaResponse> getMyQna(String username) {
        // 1. 유저 정보 가져오기
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return  null;

        // 2. 유저 정보로 Qna 가져오기
        ArrayList<Qna> qnas = qnaRepository.findByUser(user);

        // 3. response에 담기
        ArrayList<MyQnaResponse> responses = new ArrayList<>();
        for (Qna qna : qnas) {
            ArrayList<String> qnaImagesResponse = new ArrayList<>();
            for (QnaImage qnaImage : qna.getImages())
                qnaImagesResponse.add(qnaImage.getUrl());
            responses.add(new MyQnaResponse(
                    qna.getQnaId(),
                    qna.getTitle(),
                    qna.getBody(),
                    qnaImagesResponse,
                    qna.getQnaComments().size(),
                    qna.getQnaLikes().size(),
                    qna.getQnaScraps().size()
            ));
        }
        // 4. 반환
        return responses;
    }

    public ArrayList<MyScrapResponse> getMyScrap(String username) {
        // 1. 유저 정보 가져오기
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return  null;

        // 2. 유저 정보로 Qna 가져오기
        ArrayList<QnaScrap> qnaScraps = scrapRepository.findByUser(user);
        ArrayList<Qna> qnas = new ArrayList<>();
        for (QnaScrap qnaScrap : qnaScraps) qnas.add(qnaScrap.getQna());

        // 3. response에 담기
        ArrayList<MyScrapResponse> responses = new ArrayList<>();
        for (Qna qna : qnas){
            ArrayList<String> qnaImagesResponse = new ArrayList<>();
            for (QnaImage qnaImage : qna.getImages())
                qnaImagesResponse.add(qnaImage.getUrl());
            responses.add(new MyScrapResponse(
                    qna.getQnaId(),
                    qna.getTitle(),
                    qna.getBody(),
                    qnaImagesResponse,
                    qna.getQnaComments().size(),
                    qna.getQnaLikes().size(),
                    qna.getQnaScraps().size()
            ));
        }
        // 4. 반환
        return responses;
    }

    public MyBadgeResponse getMyBadge(String username) {
        return null;
    }
}
