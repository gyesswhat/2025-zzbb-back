package com.example.zzbb.qna.repository;

import com.example.zzbb.qna.entity.QnaComment;
import com.example.zzbb.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CommentRepository extends JpaRepository<QnaComment, Integer> {
    @Query("SELECT c FROM QnaComment c WHERE c.qna = :qna ORDER BY c.generatedTime ASC")
    ArrayList<QnaComment> findByUser(User user);
}
