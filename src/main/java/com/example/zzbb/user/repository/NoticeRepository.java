package com.example.zzbb.user.repository;

import com.example.zzbb.user.entity.Notice;
import com.example.zzbb.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    ArrayList<Notice> findByUserAndIsReadFalse(User user);
    ArrayList<Notice> findByUser(User user);
}
