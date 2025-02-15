package com.example.zzbb.qna.service;

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
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final ScrapRepository scrapRepository;
    private final S3Service s3Service;

    // nullpointer 조심하기
    public ArrayList<BriefQnaResponse> viewBriefQna() {
        // 1. Qna 불러오기
        ArrayList<Qna> qnas = qnaRepository.getLatestQnas(PageRequest.of(0, 10));
        if (qnas == null) return null;

        // 2. response 형식으로 변환
        ArrayList<BriefQnaResponse> responses = new ArrayList<>();
        for (Qna qna : qnas) {
            responses.add(new BriefQnaResponse(
                    qna.getQnaId(),
                    qna.getTitle()
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
            for (QnaHashtag qnaHashtag : qna.getQnaHashtags())
                qnaHashtagResponse.add(qnaHashtag.getTag());
            responses.add(new QnaListResponse(
                    qna.getQnaId(),
                    qna.getTitle(),
                    qna.getBody(),
                    qnaHashtagResponse,
                    qna.getComments().size(),
                    qna.getLikes().size(),
                    qna.getScraps().size()
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
        for (QnaHashtag hashtag : qna.getQnaHashtags())
            qnaHashtagResponse.add(hashtag.getTag());
        for (QnaImage qnaImage : qna.getImages())
            qnaImageResponse.add(qnaImage.getUrl());
        for (Comment comment : qna.getComments())
            qnaCommentResponse.add(
                    new CommentDto(
                            comment.getBody(),
                            comment.getGeneratedTime()
            ));

        // 2. response 형식으로 변환
        QnaResponse response = new QnaResponse(
                qna.getQnaId(),
                qna.getUser().getUserId(),
                qna.getTitle(),
                qna.getBody(),
                qnaHashtagResponse,
                qnaImageResponse,
                qna.getComments().size(),
                qna.getLikes().size(),
                qna.getScraps().size(),
                qnaCommentResponse
        );

        // 3. 반환
        return response;
    }

    public LikeResponse likeQna(Integer qnaId, String username) {
        Qna targetQna = qnaRepository.getReferenceById(qnaId);
        User targetUser = userRepository.findByUsername(username).orElse(null);
        if (targetQna == null || targetUser == null) return null;

        LikeResponse response = new LikeResponse();

        Optional<Likes> existingLike = likeRepository.findByUserIdAndItemId(targetUser, targetQna);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            response = new LikeResponse(false);
        }
        else {
            // 1. 새로운 Likes 생성
            Likes like = new Likes(null, targetQna, targetUser);
            // 2. 저장
            Likes saved = likeRepository.save(like);
            if (saved == null) return null;
            else response = new LikeResponse(true);
        }

        // 3. 반환
        return response;
    }

    public ScrapResponse scrapQna(Integer qnaId, String username) {
        Qna targetQna = qnaRepository.getReferenceById(qnaId);
        User targetUser = userRepository.findByUsername(username).orElse(null);
        if (targetQna == null || targetUser == null) return null;

        ScrapResponse response = new ScrapResponse();

        Optional<Scrap> existingScrap = scrapRepository.findByUserIdAndItemId(targetUser, targetQna);
        if (existingScrap.isPresent()) {
            scrapRepository.delete(existingScrap.get());
            response = new ScrapResponse(false);
        }
        else {
            // 1. 새로운 Scrap 생성
            Scrap scrap = new Scrap(null, targetQna, targetUser);
            Scrap saved = scrapRepository.save(scrap);
            // 2. 반환
            if (saved == null) return null;
            else response = new ScrapResponse(true);
        }
        // 3. 반환
        return response;
    }

    public Comment commentQna(Integer qnaId, String username, QnaCommentRequest request) {
        // 1. 새로운 Comment 생성
        Qna targetQna = qnaRepository.getReferenceById(qnaId);
        User targetUser = userRepository.findByUsername(username).orElse(null);
        if (targetQna == null || targetUser == null) return null;

        LocalDateTime localDateTime = LocalDateTime.now();
        String formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Comment comment = new Comment(
                null,
                request.getBody(),
                targetQna,
                targetUser,
                formattedTime);

        // 2. 저장
        Comment response = commentRepository.save(comment);
        if (response == null) return null;

        // 3. 반환
        return response;
    }

    public QnaPostResponse postQna(String username, QnaPostRequest request, List<MultipartFile> multipartFilelist) throws IOException {
        // 1. 새로운 Qna 생성
        User targetUser = userRepository.findByUsername(username).orElse(null);
        if (targetUser == null) return null;

        LocalDateTime localDateTime = LocalDateTime.now();
        String formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ArrayList<QnaHashtag> hashtags = new ArrayList<>();
        for (String hashtagTxt : request.getHashtags()) {
            hashtags.add(new QnaHashtag(
                    null,
                    hashtagTxt
            ));
        }
        Qna qna = new Qna(
                null,
                request.getTitle(),
                request.getBody(),
                targetUser,
                null,
                hashtags,
                new ArrayList<Comment>(),
                new ArrayList<Likes>(),
                new ArrayList<Scrap>(),
                formattedTime
        );

        // 2. 저장
        Qna saved = qnaRepository.save(qna);
        if (saved == null) return null;

        // 3. 이미지가 있다면 등록
        List<QnaImage> qnaImages = null;
        if (multipartFilelist != null && !multipartFilelist.isEmpty()) {
            qnaImages = s3Service.addReviewImages(qna, multipartFilelist);
            if (qnaImages == null) return null;
            qna.setImages(qnaImages);
            qnaRepository.save(qna);
        }

        // 3. response로 변환
        QnaPostResponse qnaPostResponse = new QnaPostResponse(saved.getQnaId());

        // 4. 반환
        return qnaPostResponse;
    }
}
