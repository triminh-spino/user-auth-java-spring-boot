package com.example.demo_testing.Model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo_testing.Enum.Role;
import com.fasterxml.jackson.annotation.JsonFormat;

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

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
    
}
