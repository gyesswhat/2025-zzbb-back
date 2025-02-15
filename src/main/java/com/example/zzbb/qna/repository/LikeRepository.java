package com.example.zzbb.qna.repository;

import com.example.zzbb.qna.entity.Likes;
import com.example.zzbb.qna.entity.Qna;
import com.example.zzbb.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Integer> {
    ArrayList<Likes> findByUser(User user);

    @Query("SELECT l FROM Likes l WHERE l.user = :targetUser AND l.qna = :targetQna")
    Optional<Likes> findByUserIdAndItemId(User targetUser, Qna targetQna);
}