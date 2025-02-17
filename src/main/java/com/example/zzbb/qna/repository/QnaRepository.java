package com.example.zzbb.qna.repository;

import com.example.zzbb.qna.entity.Qna;
import com.example.zzbb.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Integer> {
    @Query("SELECT q FROM Qna q ORDER BY q.generatedTime DESC")
    ArrayList<Qna> getLatestQnas(Pageable pageable);

    ArrayList<Qna> findByUser(User user);

    void deleteByUser(User user);
}
