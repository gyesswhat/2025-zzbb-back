package com.example.zzbb.db.repository;

import com.example.zzbb.db.entity.Db;
import com.example.zzbb.db.entity.DbLike;
import com.example.zzbb.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface DbLikeRepository extends JpaRepository<DbLike, Integer> {
    @Query("SELECT l FROM DbLike l WHERE l.user = :targetUser AND l.db = :targetDb")
    Optional<DbLike> findByUserIdAndItemId(User targetUser, Db targetDb);

    ArrayList<DbLike> findByUser(User user);
}
