package com.example.zzbb.db.service;

import com.example.zzbb.db.dto.*;
import com.example.zzbb.db.entity.Db;
import com.example.zzbb.db.entity.DbImage;
import com.example.zzbb.db.entity.DbLike;
import com.example.zzbb.db.entity.DbScrap;
import com.example.zzbb.db.repository.DbLikeRepository;
import com.example.zzbb.db.repository.DbRepository;
import com.example.zzbb.db.repository.DbScrapRepository;
import com.example.zzbb.hashtag.Hashtag;
import com.example.zzbb.qna.dto.QnaLikeResponse;
import com.example.zzbb.qna.dto.QnaScrapResponse;
import com.example.zzbb.qna.entity.Qna;
import com.example.zzbb.qna.entity.QnaLike;
import com.example.zzbb.qna.entity.QnaScrap;
import com.example.zzbb.user.entity.User;
import com.example.zzbb.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbService {
    private final DbRepository dbRepository;
    private final DbLikeRepository dbLikeRepository;
    private final DbScrapRepository dbScrapRepository;
    private final UserRepository userRepository;

    public ArrayList<BriefDbResponse> viewBriefDb() {
        // 1. Db 불러오기
        ArrayList<Db> dbs = dbRepository.getLatestDbs(PageRequest.of(0, 10));
        if (dbs == null) return null;

        // 2. response 형식으로 변환
        ArrayList<BriefDbResponse> responses = new ArrayList<>();
        for (Db db : dbs) {
            responses.add(new BriefDbResponse(
                    db.getDbId(),
                    db.getTitle(),
                    db.getGeneratedTime()
            ));
        }

        // 3. 반환
        return responses;
    }

    public ArrayList<DbListResponse> viewDbList() {
        // 1. Db 불러오기
        ArrayList<Db> dbs = (ArrayList<Db>) dbRepository.findAll();
        if (dbs == null) return null;

        // 2. response 형식으로 변환
        ArrayList<DbListResponse> responses = new ArrayList<>();
        for (Db db: dbs) {
            ArrayList<String> dbHashtagResponse = new ArrayList<>();
            for (Hashtag hashtag : db.getHashtags())
                dbHashtagResponse.add(hashtag.getTag());
            responses.add(new DbListResponse(
                    db.getDbId(),
                    db.getTitle(),
                    db.getBody(),
                    dbHashtagResponse,
                    db.getDbLikes().size(),
                    db.getDbScraps().size()
            ));
        }

        // 3. 반환
        return responses;
    }

    public DbResponse viewDb(Integer dbId) {
        // 1. Db 불러오기
        Db db = dbRepository.findById(dbId).orElse(null);
        if (db == null) return null;

        ArrayList<String> dbHashTagResponse = new ArrayList<>();
        ArrayList<String> dbImageResponse = new ArrayList<>();
        for (Hashtag hashtag : db.getHashtags())
            dbHashTagResponse.add(hashtag.getTag());
        for (DbImage dbImage : db.getImages())
            dbImageResponse.add(dbImage.getUrl());

        // 2. response 형식으로 변환
        DbResponse response = new DbResponse(
                db.getDbId(),
                db.getTitle(),
                db.getBody(),
                dbHashTagResponse,
                dbImageResponse,
                db.getDbLikes().size(),
                db.getDbScraps().size()
        );

        // 3. 반환
        return response;
    }

    public DbLikeResponse likeDb(Integer qnaId, String username) {
        Db targetDb = dbRepository.getReferenceById(qnaId);
        User targetUser = userRepository.findByUsername(username).orElse(null);
        if (targetDb == null || targetUser == null) return null;

        DbLikeResponse response = new DbLikeResponse();

        Optional<DbLike> existingDbLike = dbLikeRepository.findByUserIdAndItemId(targetUser, targetDb);
        if (existingDbLike.isPresent()) {
            dbLikeRepository.delete(existingDbLike.get());
            response = new DbLikeResponse(false);
        }
        else {
            // 1. 새로운 Likes 생성
            DbLike like = new DbLike(null, targetDb, targetUser);
            dbLikeRepository.save(like);
            targetUser.setScore(targetUser.getScore() + 5);
            response = new DbLikeResponse(true);
        }

        // 3. 반환
        userRepository.save(targetUser);
        return response;
    }

    public DbScrapResponse scrapDb(Integer qnaId, String username) {
        Db targetDb = dbRepository.getReferenceById(qnaId);
        User targetUser = userRepository.findByUsername(username).orElse(null);
        if (targetDb == null || targetUser == null) return null;

        DbScrapResponse response = new DbScrapResponse();

        Optional<DbScrap> existingDbScrap = dbScrapRepository.findByUserIdAndItemId(targetUser, targetDb);
        if (existingDbScrap.isPresent()) {
            dbScrapRepository.delete(existingDbScrap.get());
            response = new DbScrapResponse(false);
        }
        else {
            // 1. 새로운 Scrap 생성
            DbScrap dbScrap = new DbScrap(null, targetDb, targetUser);
            dbScrapRepository.save(dbScrap);
            targetUser.setScore(targetUser.getScore() + 5);
            response = new DbScrapResponse(true);
        }
        // 3. 반환
        userRepository.save(targetUser);
        return response;
    }
}
