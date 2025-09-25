package com.taxshelf.taxshelf_api.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.taxshelf.taxshelf_api.config.JwtConfig;
import com.taxshelf.taxshelf_api.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;
    
    public Jwt generateToken(User user, int tokenExpiration){
        var claims =  Jwts.claims()
        .subject(user.getId().toString())
        .add("email", user.getEmail())
        .add("userName", user.getUserName())
        .add("roleType", user.getRole().getName())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
        .build();

        return new Jwt(claims, jwtConfig.getSecretKey());
     
    }

    public Jwt generateAccessToken(User user){
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }
    public Jwt generateRefreshToken(User user){
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
     }
     public Jwt parseToken(String token){
        try {
            var claims = getClaims(token);
            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (JwtException e) {
            // TODO: handle exception
            return null;
        }
     }
     private Claims getClaims(String token){
        return Jwts.parser()
            .verifyWith(jwtConfig.getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
