package com.example.zzbb.db.repository;

import com.example.zzbb.db.entity.Db;
import com.example.zzbb.db.entity.DbScrap;
import com.example.zzbb.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DbScrapRepository extends JpaRepository<DbScrap, Integer> {
    @Query("SELECT s FROM DbScrap s WHERE s.user = :targetUser AND s.db = :targetDb")
    Optional<DbScrap> findByUserIdAndItemId(User targetUser, Db targetDb);
}
