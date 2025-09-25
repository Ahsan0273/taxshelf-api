package com.taxshelf.taxshelf_api.services;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Jwt {
    private final Claims claims;
    private final SecretKey secretKey;
    
    public boolean isExpired(){
        return claims.getExpiration().before(new Date());
    }

    public String getUserId(){
       return claims.getSubject();
    }
    public String getRoleType(){
        return claims.get("roleType", String.class);
     }

     public String toString(){
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
     }
   
}
