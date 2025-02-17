package com.example.zzbb.qna.repository;

import com.example.zzbb.qna.entity.Qna;
import com.example.zzbb.qna.entity.QnaScrap;
import com.example.zzbb.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface QnaScrapRepository extends JpaRepository<QnaScrap, Integer> {
    ArrayList<QnaScrap> findByUser(User user);

    @Query("SELECT s FROM QnaScrap s WHERE s.user = :targetUser AND s.qna = :targetQna")
    Optional<QnaScrap> findByUserIdAndItemId(User targetUser, Qna targetQna);
}
