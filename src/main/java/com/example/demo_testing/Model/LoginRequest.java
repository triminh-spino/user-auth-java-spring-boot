package com.example.demo_testing.Model;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginRequest {
    @Email(message = "This is not email.")
    @NotEmpty(message = "Email is empty.")
    private String email;

    @Length(min = 8, message = "Password must at least 8 character long.")
    @NotEmpty(message = "Password is empty.")
    private String password;
}
