package com.example.zzbb.qna.repository;

import com.example.zzbb.qna.entity.Comment;
import com.example.zzbb.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c FROM Comment c WHERE c.qna = :qna ORDER BY c.generatedTime ASC")
    ArrayList<Comment> findByUser(User user);
}
