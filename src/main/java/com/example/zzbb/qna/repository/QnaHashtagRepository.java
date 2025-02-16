package com.example.zzbb.qna.repository;

import com.example.zzbb.qna.entity.QnaHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QnaHashtagRepository extends JpaRepository<QnaHashtag, Integer> {
    Optional<QnaHashtag> findByTag(java.lang.String tag);
}
