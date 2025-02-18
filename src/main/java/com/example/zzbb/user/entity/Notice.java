package com.example.zzbb.user.entity;

import com.example.zzbb.qna.entity.Qna;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 알림 받는 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id", nullable = false)
    private Qna qna;

    @Column(nullable = false)
    private Boolean isScraped;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String commentBody;

    @Column(nullable = false)
    private boolean isRead = false;  // 읽음 여부

    @Column(nullable = false)
    private String createdTime;
}
