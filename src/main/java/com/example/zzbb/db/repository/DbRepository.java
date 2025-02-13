package com.example.zzbb.db.repository;

import com.example.zzbb.db.entity.Db;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbRepository extends JpaRepository<Db, Integer> {
}
