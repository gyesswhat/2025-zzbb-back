package com.example.zzbb.qna.service;

import com.example.zzbb.hashtag.Hashtag;
import com.example.zzbb.hashtag.HashtagRepository;
import com.example.zzbb.qna.dto.*;
import com.example.zzbb.qna.entity.*;
import com.example.zzbb.qna.repository.*;
import com.example.zzbb.user.entity.User;
import com.example.zzbb.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QnaService {
    private final QnaRepository qnaRepository;
    private final HashtagRepository hashtagRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final QnaLikeRepository qnaLikeRepository;
    private final QnaScrapRepository qnaScrapRepository;
    private final S3Service s3Service;

    public ArrayList<BriefQnaResponse> viewBriefQna() {
        // 1. Qna 불러오기
        ArrayList<Qna> qnas = qnaRepository.getLatestQnas(PageRequest.of(0, 10));
        if (qnas == null) return null;

        // 2. response 형식으로 변환
        ArrayList<BriefQnaResponse> responses = new ArrayList<>();
        for (Qna qna : qnas) {
            responses.add(new BriefQnaResponse(
                    qna.getQnaId(),
                    qna.getTitle(),
                    (!qna.getImages().isEmpty())? qna.getImages().get(0).getUrl() : null,
                    qna.getGeneratedTime()
            ));
        }

        // 3. 반환
        return responses;
    }

    public ArrayList<QnaListResponse> viewQnaList() {
        // 1. Qna 불러오기
        ArrayList<Qna> qnas = (ArrayList<Qna>) qnaRepository.findAll();
        if (qnas == null) return null;

        // 2. response 형식으로 변환
        ArrayList<QnaListResponse> responses = new ArrayList<>();
        for (Qna qna : qnas) {
            ArrayList<String> qnaHashtagResponse = new ArrayList<>();
            for (Hashtag hashtag : qna.getQnaHashtags())
                qnaHashtagResponse.add(hashtag.getTag());
            responses.add(new QnaListResponse(
                    qna.getQnaId(),
                    qna.getTitle(),
                    qna.getBody(),
                    qnaHashtagResponse,
                    qna.getQnaComments().size(),
                    qna.getQnaLikes().size(),
                    qna.getQnaScraps().size()
            ));
        }

        // 3. 반환
        return responses;
    }

    public QnaResponse viewQna(Integer qnaId) {
        // 1. Qna 불러오기
        Qna qna = qnaRepository.findById(qnaId).orElse(null);
        if (qna == null) return null;

        // 2. 객체 처리
        ArrayList<String> qnaHashtagResponse = new ArrayList<>();
        ArrayList<String> qnaImageResponse = new ArrayList<>();
        ArrayList<CommentDto> qnaCommentResponse = new ArrayList<>();
        for (Hashtag hashtag : qna.getQnaHashtags())
            qnaHashtagResponse.add(hashtag.getTag());
        for (QnaImage qnaImage : qna.getImages())
            qnaImageResponse.add(qnaImage.getUrl());
        for (QnaComment qnaComment : qna.getQnaComments())
            qnaCommentResponse.add(
                    new CommentDto(
                            qnaComment.getUser().getNickname(),
                            qnaComment.getBody(),
                            qnaComment.getGeneratedTime()
            ));

        // 2. response 형식으로 변환
        QnaResponse response = new QnaResponse(
                qna.getQnaId(),
                qna.getUser().getUserId(),
                qna.getTitle(),
                qna.getBody(),
                qnaHashtagResponse,
                qnaImageResponse,
                qna.getQnaComments().size(),
                qna.getQnaLikes().size(),
                qna.getQnaScraps().size(),
                qnaCommentResponse
        );

        // 3. 반환
        return response;
    }

    public QnaLikeResponse likeQna(Integer qnaId, String username) {
        Qna targetQna = qnaRepository.getReferenceById(qnaId);
        User targetUser = userRepository.findByUsername(username).orElse(null);
        if (targetQna == null || targetUser == null) return null;

        QnaLikeResponse response = new QnaLikeResponse();

        Optional<QnaLike> existingQnaLike = qnaLikeRepository.findByUserIdAndItemId(targetUser, targetQna);
        if (existingQnaLike.isPresent()) {
            qnaLikeRepository.delete(existingQnaLike.get());
            response = new QnaLikeResponse(false);
        }
        else {
            // 1. 새로운 Likes 생성
            QnaLike like = new QnaLike(null, targetQna, targetUser);
            // 2. 저장
            QnaLike saved = qnaLikeRepository.save(like);
            if (saved == null) return null;
            else response = new QnaLikeResponse(true);
        }

        // 3. 반환
        return response;
    }

    public QnaScrapResponse scrapQna(Integer qnaId, String username) {
        Qna targetQna = qnaRepository.getReferenceById(qnaId);
        User targetUser = userRepository.findByUsername(username).orElse(null);
        if (targetQna == null || targetUser == null) return null;

        QnaScrapResponse response = new QnaScrapResponse();

        Optional<QnaScrap> existingScrap = qnaScrapRepository.findByUserIdAndItemId(targetUser, targetQna);
        if (existingScrap.isPresent()) {
            qnaScrapRepository.delete(existingScrap.get());
            response = new QnaScrapResponse(false);
        }
        else {
            // 1. 새로운 Scrap 생성
            QnaScrap qnaScrap = new QnaScrap(null, targetQna, targetUser);
            QnaScrap saved = qnaScrapRepository.save(qnaScrap);
            // 2. 반환
            if (saved == null) return null;
            else response = new QnaScrapResponse(true);
        }
        // 3. 반환
        return response;
    }

    public QnaComment commentQna(Integer qnaId, String username, QnaCommentRequest request) {
        // 1. 새로운 Comment 생성
        Qna targetQna = qnaRepository.getReferenceById(qnaId);
        User targetUser = userRepository.findByUsername(username).orElse(null);
        if (targetQna == null || targetUser == null) return null;

        LocalDateTime localDateTime = LocalDateTime.now();
        String formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        QnaComment qnaComment = new QnaComment(
                null,
                request.getBody(),
                targetQna,
                targetUser,
                formattedTime);

        // 2. 저장
        QnaComment response = commentRepository.save(qnaComment);
        if (response == null) return null;

        // 3. 반환
        return response;
    }

    public QnaPostResponse postQna(String username, QnaPostRequest request, List<MultipartFile> multipartFilelist) throws IOException {
        // 1. 사용자 정보 확인
        User targetUser = userRepository.findByUsername(username).orElse(null);
        if (targetUser == null || !targetUser.getIsNewbie()) return null;

        // 2. 질문 생성
        LocalDateTime localDateTime = LocalDateTime.now();
        String formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 3. 태그 처리: 이미 존재하는 태그를 찾거나 새로 생성하여 리스트에 추가
        ArrayList<Hashtag> hashtags = new ArrayList<>();
        for (String hashtagTxt : request.getHashtags()) {
            Hashtag hashtag = hashtagRepository.findByTag(hashtagTxt).orElse(null);
            if (hashtag == null) return null;
            hashtags.add(hashtag);
        }

        // 4. Qna 객체 생성
        Qna qna = new Qna(
                null,
                request.getTitle(),
                request.getBody(),
                targetUser,
                null,
                hashtags,
                new ArrayList<QnaComment>(),
                new ArrayList<QnaLike>(),
                new ArrayList<QnaScrap>(),
                formattedTime
        );

        // 5. Qna 저장
        Qna saved = qnaRepository.save(qna);
        if (saved == null) return null;

        // 6. 이미지가 있다면 S3에 업로드
        List<QnaImage> qnaImages = null;
        if (multipartFilelist != null && !multipartFilelist.isEmpty()) {
            qnaImages = s3Service.addReviewImages(qna, multipartFilelist);
            if (qnaImages == null) return null;
            qna.setImages(qnaImages);  // 저장된 이미지 연결
            qnaRepository.save(qna);    // 다시 저장하여 이미지 정보 반영
        }

        // 7. QnaPostResponse 생성 및 반환
        QnaPostResponse qnaPostResponse = new QnaPostResponse(saved.getQnaId());
        return qnaPostResponse;
    }

}
