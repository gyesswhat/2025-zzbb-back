package com.example.zzbb.qna.repository;

import com.example.zzbb.qna.entity.QnaLike;
import com.example.zzbb.qna.entity.Qna;
import com.example.zzbb.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface QnaLikeRepository extends JpaRepository<QnaLike, Integer> {
    ArrayList<QnaLike> findByUser(User user);

    @Query("SELECT l FROM QnaLike l WHERE l.user = :targetUser AND l.qna = :targetQna")
    Optional<QnaLike> findByUserIdAndItemId(User targetUser, Qna targetQna);

    void deleteByUser(User user);
}