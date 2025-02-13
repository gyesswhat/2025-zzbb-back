package com.example.zzbb.db.repository;

import com.example.zzbb.db.entity.DbImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbImageRepository extends JpaRepository<DbImage, Integer> {
}
