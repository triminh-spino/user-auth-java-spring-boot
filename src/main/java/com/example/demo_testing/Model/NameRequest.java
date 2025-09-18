package com.example.demo_testing.Model;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class NameRequest {
    @NotEmpty(message = "Name is empty.")
    @Length(min = 2, max = 100, message = "Name must be at least 2 characters long and not longer than 100 characters.")
    private String name;
}
