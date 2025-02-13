package com.example.zzbb.qna.repository;

import com.example.zzbb.qna.entity.QnaImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaImageRepository extends JpaRepository<QnaImage, Integer> {
}
