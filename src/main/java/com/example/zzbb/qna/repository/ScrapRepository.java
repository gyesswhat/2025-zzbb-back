package com.example.zzbb.qna.repository;

import com.example.zzbb.qna.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Integer> {
}
