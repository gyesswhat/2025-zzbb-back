package com.example.zzbb.qna.repository;

import com.example.zzbb.qna.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Integer> {
}
