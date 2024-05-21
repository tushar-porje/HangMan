package com.hangman.hangman_apis.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor@AllArgsConstructor
public class LoginDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
