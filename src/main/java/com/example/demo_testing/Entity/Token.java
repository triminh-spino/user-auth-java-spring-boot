package com.example.demo_testing.Entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import com.example.demo_testing.Enum.TokenType;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "tokens")
public class Token {
   
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
 
    @Column(unique = true, name = "token", nullable = false)
    private String token;
 
    @Enumerated(EnumType.ORDINAL)
    private TokenType tokenType;
 
    @Column(name = "revoked")
    private boolean revoked;
 
    @Column(name = "expired")
    private Date expired;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
 
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
 
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
 
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
