package com.example.zzbb.user.entity;

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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column
    private Boolean isNewbie;

    @Column(unique = true)
    private String username;

    @Column
    private String nickname;

    @Column
    private String password;

    @Column
    private Integer level;

    @Column
    private Integer score;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RefreshToken refreshToken; // 양방향 관계 추가
}
