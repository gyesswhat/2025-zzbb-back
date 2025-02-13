package com.example.zzbb.db.entity;

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
public class DbHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dbHashtagId;

    @Column(nullable = false, unique = true)
    private String tag;
}