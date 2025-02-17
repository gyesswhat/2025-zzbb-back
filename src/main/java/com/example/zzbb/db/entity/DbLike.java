package com.example.zzbb.db.entity;

import com.example.zzbb.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DbLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "db_id", nullable = false)
    private Db db;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
