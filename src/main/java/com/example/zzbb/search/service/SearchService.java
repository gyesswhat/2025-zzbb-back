package com.example.zzbb.search.service;

import com.example.zzbb.db.dto.DbListResponse;
import com.example.zzbb.db.entity.Db;
import com.example.zzbb.db.repository.DbRepository;
import com.example.zzbb.hashtag.Hashtag;
import com.example.zzbb.qna.dto.QnaListResponse;
import com.example.zzbb.qna.entity.Qna;
import com.example.zzbb.qna.repository.QnaRepository;
import com.example.zzbb.search.dto.SearchRequest;
import com.example.zzbb.search.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final QnaRepository qnaRepository;
    private final DbRepository dbRepository;

    public SearchResponse search(SearchRequest request) {
        ArrayList<Qna> qnas = qnaRepository.findQnaByHashtags(request.getHashtags());
        ArrayList<Db> dbs = dbRepository.findDbByHashtags(request.getHashtags());

        ArrayList<QnaListResponse> qnaResponses = new ArrayList<>();
        ArrayList<DbListResponse> dbResponses = new ArrayList<>();
        for (Qna qna : qnas) {
            ArrayList<String> hashtags = new ArrayList<>();
            for (Hashtag hashtag : qna.getQnaHashtags())
                hashtags.add(hashtag.getTag());
            qnaResponses.add(new QnaListResponse(
                    qna.getQnaId(),
                    qna.getTitle(),
                    qna.getBody(),
                    hashtags,
                    qna.getQnaComments().size(),
                    qna.getQnaLikes().size(),
                    qna.getQnaScraps().size()
            ));
        }
        for (Db db : dbs) {
            ArrayList<String> hashtags = new ArrayList<>();
            for (Hashtag hashtag: db.getHashtags())
                hashtags.add(hashtag.getTag());
            dbResponses.add(new DbListResponse(
                    db.getDbId(),
                    db.getTitle(),
                    db.getBody(),
                    hashtags,
                    db.getDbLikes().size(),
                    db.getDbScraps().size()
            ));
        }

        return new SearchResponse(
                request.getHashtags(),
                qnaResponses,
                dbResponses
        );
    }
}
