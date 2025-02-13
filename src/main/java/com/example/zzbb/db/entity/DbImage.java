package com.example.zzbb.db.entity;

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
public class DbImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dbImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "db_id", nullable = false)
    private Db db;

    @Column(nullable = false)
    private String url;
}