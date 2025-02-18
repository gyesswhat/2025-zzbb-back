package com.example.zzbb.user.service;

import com.example.zzbb.user.dto.notice.NoticeCountResponse;
import com.example.zzbb.user.dto.notice.NoticeReadResponse;
import com.example.zzbb.user.dto.notice.NoticeResponse;
import com.example.zzbb.user.entity.Notice;
import com.example.zzbb.user.entity.User;
import com.example.zzbb.user.repository.NoticeRepository;
import com.example.zzbb.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;

    public ArrayList<NoticeResponse> getMyNotice(String username) {
        // 1. 필요한 정보 찾기
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException(""));
        ArrayList<Notice> notices = noticeRepository.findByUser(user);
        // 2. response에 담기
        ArrayList<NoticeResponse> responses = new ArrayList<>();
        for (Notice notice : notices)
            responses.add(new NoticeResponse(
                    notice.getId(),
                    notice.getQna().getQnaId(),
                    notice.getIsScraped(),
                    notice.getTitle(),
                    notice.getCommentBody(),
                    notice.getCreatedTime(),
                    notice.isRead()
            ));
        // 3. 리턴
        return responses;
    }

    public NoticeCountResponse getMyNoticeCount(String username) {
        // 1. 필요한 정보 찾기
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException(""));
        ArrayList<Notice> notices = noticeRepository.findByUserAndIsReadFalse(user);
        // 2. response에 담기
        NoticeCountResponse response = new NoticeCountResponse(notices.size());
        // 3. 리턴
        return response;
    }

    public NoticeReadResponse readMyNotice(String username, Integer noticeId) {
        // 1. 필요한 정보 찾기
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("사용자를 찾을 수 없습니다: " + username));
        // 2. 읽기
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("알림을 찾을 수 없습니다: " + noticeId));
        // 2-1. Qna가 null일 경우 처리
        if (notice.getQna() == null) {
            throw new EntityNotFoundException("알림에 연결된 QnA를 찾을 수 없습니다.");
        }
        notice.setRead(true);
        noticeRepository.save(notice);
        // 3. response에 담기
        NoticeReadResponse response = new NoticeReadResponse(notice.getQna().getQnaId());
        // 4. 리턴
        return response;
    }
}
