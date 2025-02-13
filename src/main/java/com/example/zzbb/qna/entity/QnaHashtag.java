package com.example.zzbb.qna.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QnaHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer qnaHashtagId;

    @Column(nullable = false, unique = true)
    private String tag;
}
