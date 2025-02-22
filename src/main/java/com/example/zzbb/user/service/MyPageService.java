package com.example.zzbb.user.service;

import com.example.zzbb.db.entity.Db;
import com.example.zzbb.db.entity.DbLike;
import com.example.zzbb.db.entity.DbScrap;
import com.example.zzbb.db.repository.DbLikeRepository;
import com.example.zzbb.db.repository.DbRepository;
import com.example.zzbb.db.repository.DbScrapRepository;
import com.example.zzbb.qna.entity.*;
import com.example.zzbb.qna.repository.CommentRepository;
import com.example.zzbb.qna.repository.QnaLikeRepository;
import com.example.zzbb.qna.repository.QnaRepository;
import com.example.zzbb.qna.repository.QnaScrapRepository;
import com.example.zzbb.user.dto.mypage.MyLevelResponse;
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
    private final QnaLikeRepository qnaLikeRepository;
    private final QnaScrapRepository qnaScrapRepository;
    private final DbLikeRepository dbLikeRepository;
    private final DbScrapRepository dbScrapRepository;
    private final UserRepository userRepository;

    public MyHistoryResponse getMyHistory(String username) {
        // 1. 유저 정보 가져오기
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return  null;

        // 2. 작성글, 스크랩글 정보 받아오기
        ArrayList<Qna> wroteArticles = qnaRepository.findByUser(user);
        ArrayList<QnaScrap> scrapedQnas = qnaScrapRepository.findByUser(user);
        ArrayList<DbScrap> scrapedDbs = dbScrapRepository.findByUser(user);

        // 3. response에 담기
        MyHistoryResponse response = new MyHistoryResponse(
                wroteArticles.size(),
                scrapedQnas.size() + scrapedDbs.size()
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
            responses.add(new MyQnaResponse(
                    qna.getQnaId(),
                    qna.getTitle(),
                    qna.getBody(),
                    qna.getQnaComments().size(),
                    qna.getQnaLikes().size(),
                    qna.getQnaScraps().size()
            ));
        }
        // 4. 반환
        return responses;
    }

    public MyScrapResponse getMyScrap(String username) {
        // 1. 유저 정보 가져오기
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return  null;

        // 2. 유저 정보로 Qna 가져오기
        ArrayList<QnaScrap> qnaScraps = qnaScrapRepository.findByUser(user);
        ArrayList<Qna> qnas = new ArrayList<>();
        for (QnaScrap qnaScrap : qnaScraps) qnas.add(qnaScrap.getQna());

        // 3. 유저 정보로 Db 가져오기
        ArrayList<DbScrap> dbScraps = dbScrapRepository.findByUser(user);
        ArrayList<Db> dbs = new ArrayList<>();
        for (DbScrap dbScrap : dbScraps) dbs.add(dbScrap.getDb());

        // 4. response에 넣기
        ArrayList<MyQnaScrap> myQnaScraps = new ArrayList<>();
        for (Qna qna : qnas)
            myQnaScraps.add(new MyQnaScrap(
                    qna.getQnaId(),
                    qna.getTitle(),
                    qna.getBody(),
                    qna.getQnaComments().size(),
                    qna.getQnaLikes().size(),
                    qna.getQnaScraps().size()
            ));
        ArrayList<MyDbScrap> myDbScraps = new ArrayList<>();
        for (Db db: dbs)
            myDbScraps.add(new MyDbScrap(
                    db.getDbId(),
                    db.getTitle(),
                    db.getBody(),
                    db.getDbLikes().size(),
                    db.getDbScraps().size()
            ));

        MyScrapResponse response = new MyScrapResponse(myQnaScraps, myDbScraps);
        return response;
    }

    public MyBadgeResponse getMyBadge(String username) {
        // 1. 유저 정보 가져오기
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return  null;

        // 2. 유저 정보 담기
        ArrayList<Qna> questions = qnaRepository.findByUser(user);
        ArrayList<QnaComment> comments = commentRepository.findByUser(user);
        ArrayList<QnaLike> qnaLikes = qnaLikeRepository.findByUser(user);
        ArrayList<DbLike> dbLikes = dbLikeRepository.findByUser(user);
        ArrayList<QnaScrap> qnaScraps = qnaScrapRepository.findByUser(user);
        ArrayList<DbScrap> dbScraps = dbScrapRepository.findByUser(user);
        MyBadgeResponse response = new MyBadgeResponse(
                user.getLevel(),
                user.getNickname(),
                questions.size(),
                comments.size(),
                qnaLikes.size() + dbLikes.size() + qnaScraps.size() + dbScraps.size()
        );
        return response;
    }

    public MyLevelResponse getMyLevel(String username) {
        // 1. 유저 정보 가져오기
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return null;

        // 2. 현재 레벨과 점수 가져오기
        int currentScore = user.getScore();
        int currentLevel = user.getLevel();
        int nextLevelScore = getNextLevelScore(currentLevel); // 다음 레벨 기준 점수 가져오기

        // 3. 레벨 업 체크
        while (nextLevelScore > 0 && currentScore >= nextLevelScore) {
            currentLevel++; // 레벨 업
            nextLevelScore = getNextLevelScore(currentLevel); // 새로운 다음 레벨 점수
        }

        // 4. 유저 정보 업데이트
        if (currentLevel != user.getLevel()) { // 레벨이 변경된 경우만 업데이트
            user.setLevel(currentLevel);
            userRepository.save(user); // DB 반영
        }

        // 5. 응답 생성 및 리턴
        MyLevelResponse response = new MyLevelResponse();
        response.setCurrentScore(currentScore);
        response.setNextLevelScore(nextLevelScore);

        return response;
    }

    // 레벨별 기준 점수를 반환하는 메서드
    private int getNextLevelScore(int level) {
        switch (level) {
            case 1: return 100;
            case 2: return 300;
            default: return 0; // 최대 레벨 도달 시 0 반환 (레벨 업 없음)
        }
    }
}
