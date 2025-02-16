package com.example.zzbb.db.repository;

import com.example.zzbb.db.entity.Db;
import com.example.zzbb.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface DbRepository extends JpaRepository<Db, Integer> {
    @Query("SELECT d FROM Db d ORDER BY d.dbId DESC")
    ArrayList<Db> getLatestDbs(Pageable pageable);
}
