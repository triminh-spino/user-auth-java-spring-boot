package com.example.demo_testing.Service;
 
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo_testing.Entity.Token;
import com.example.demo_testing.Enum.TokenType;
import com.example.demo_testing.Exception.ForbiddenException;
import com.example.demo_testing.Exception.NotFoundException;
import com.example.demo_testing.Exception.UnauthorizedException;
import com.example.demo_testing.Repository.TokenRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;
 
@Service
public class JwtService {
 
    @Value("${security.jwt.token.secret}")
    private String SECRET_KEY;
 
    @Value("${security.jwt.expire}")
    private Long expireTokenTime;
 
    @Autowired
    private TokenRepository tokenRepository;
 
    public String extractUserName(String token, TokenType tokenType) {
        return extractClaim(token, tokenType, Claims::getSubject);
    }
 
    public boolean checkTokenRevoked(String token, TokenType tokenType) {
        Token getToken = tokenRepository.findByTokenAndTokenType(token, tokenType).orElseThrow(
            () -> new NotFoundException("This specific token type not found.")
        );
       
        if (getToken.isRevoked()) {
            throw new ForbiddenException("This specific token type is revoked.");
        } else if (getToken.getExpired().before(new Date())) {
            throw new UnauthorizedException("This specific token type is expired.");
        }
 
        return true;
    }
 
    public boolean isTokenValid(String token, UserDetails userDetails, TokenType tokenType) {
        final String username = extractUserName(token, tokenType);
        return username.equals(userDetails.getUsername());
    }
 
    public Date extractExpiration(String token, TokenType tokenType) {
        return getExpiration(token, tokenType);
    }
 
    private Date getExpiration(String token, TokenType tokenType) {
        return extractClaim(token, tokenType, Claims::getExpiration);
    }
 
    public String generateToken(UserDetails userDetails, TokenType tokenType) {
        return buildToken(new HashMap<>(), userDetails, getExpireTime(tokenType), tokenType);
    }
 
    private Long getExpireTime(TokenType tokenType) {
        switch (tokenType) {
            default:
                return expireTokenTime;
        }
    }
 
    public String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, Long expire, TokenType tokenType) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expire))
                .signWith(getSigningKey(tokenType))
                .compact();
    }
 
    private SecretKey getSigningKey(TokenType tokenType) {
        String key = switch (tokenType) {
            default -> SECRET_KEY;
        };
 
        byte[] keyBytes = Decoders.BASE64.decode(key);
 
        return Keys.hmacShaKeyFor(keyBytes);
    }
 
    public <T> T extractClaim(String token, TokenType tokenType, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(token, tokenType);
        return claimResolver.apply(claims);
    }
 
    private Claims extractAllClaims(String token, TokenType tokenType) {
        return Jwts.parser()
                .verifyWith(getSigningKey(tokenType))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}