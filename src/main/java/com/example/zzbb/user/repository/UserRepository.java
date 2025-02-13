package com.example.zzbb.user.repository;

import com.example.zzbb.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT EXISTS(SELECT 1 FROM user WHERE username = :username)", nativeQuery = true)
    Long existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
