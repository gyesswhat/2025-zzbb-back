package com.example.zzbb.qna.repository;

import com.example.zzbb.qna.entity.Qna;
import com.example.zzbb.qna.entity.Scrap;
import com.example.zzbb.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Integer> {
    ArrayList<Scrap> findByUser(User user);

    @Query("SELECT s FROM Scrap s WHERE s.user = :targetUser AND s.qna = :targetQna")
    Optional<Scrap> findByUserIdAndItemId(User targetUser, Qna targetQna);
}
