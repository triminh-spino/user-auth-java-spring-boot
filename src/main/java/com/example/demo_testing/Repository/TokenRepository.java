package com.example.demo_testing.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo_testing.Entity.Token;
import com.example.demo_testing.Enum.TokenType;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    @Query(value = """
            SELECT To FROM Token To INNER JOIN User Us
            ON To.user.id = Us.id
            WHERE Us.id = :id
            AND To.expired > CURRENT_TIMESTAMP
            AND (To.revoked = false)
            AND (To.tokenType = :type)
            """)
    List<Token> findAllValidTokenByUser(UUID id, TokenType type);

    Optional<Token> findByTokenAndTokenType(String token, TokenType type);
}
