package com.example.zzbb.user.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {
    @NotBlank
    private String username;

    @NotNull
    private Boolean isNewbie;

    @NotBlank
    private String nickname;

    @NotBlank
    private String password;
}
