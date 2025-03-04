package com.example.zzbb.qna.repository;

import com.example.zzbb.qna.entity.Qna;
import com.example.zzbb.qna.entity.QnaComment;
import com.example.zzbb.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<QnaComment, Integer> {
    @Query("SELECT c FROM QnaComment c WHERE c.user = :user ORDER BY c.generatedTime ASC")
    ArrayList<QnaComment> findByUser(User user);

    @Query("SELECT c FROM QnaComment c WHERE c.user = :targetUser AND c.qna = :targetQna")
    ArrayList<QnaComment> findByUserIdAndItemId(User targetUser, Qna targetQna);
}
