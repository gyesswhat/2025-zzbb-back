package com.example.zzbb.db.entity;

import com.example.zzbb.hashtag.Hashtag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Db {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dbId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @OneToMany(mappedBy = "db", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DbImage> images = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "db_hashtag_mapping",
            joinColumns = @JoinColumn(name = "db_id"),
            inverseJoinColumns = @JoinColumn(name = "db_hashtag_id")
    )
    private List<Hashtag> hashtags = new ArrayList<>();

    @OneToMany(mappedBy = "db", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DbLike> dbLikes = new ArrayList<>();

    @OneToMany(mappedBy = "db", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DbScrap> dbScraps = new ArrayList<>();

    @Column(nullable = false)
    private String generatedTime;
}
