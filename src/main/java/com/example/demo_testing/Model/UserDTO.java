package com.example.demo_testing.Model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo_testing.Enum.Role;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {
    private UUID id;
    private String email;
    private String name;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
