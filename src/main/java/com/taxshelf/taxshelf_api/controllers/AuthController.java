package com.taxshelf.taxshelf_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taxshelf.taxshelf_api.dtos.JwtResponse;
import com.taxshelf.taxshelf_api.dtos.LoginRequest;
import com.taxshelf.taxshelf_api.dtos.UserDto;
import com.taxshelf.taxshelf_api.repositories.UserRepository;
import com.taxshelf.taxshelf_api.services.JwtService;
import com.taxshelf.taxshelf_api.services.UserMapper;

import jakarta.validation.Valid;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response){
       
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true); // means it cannot be access by javascript
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(86400);
        cookie.setSecure(true); //will only be send on https channel

        response.addCookie(cookie);
        // validate passowrd and return token
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @GetMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue( value= "refreshToken") String refreshToken){
        var jwt = jwtService.parseToken(refreshToken);
        if(jwt == null || jwt.isExpired()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } 
        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @GetMapping("/authenticate")
    public Boolean authenticate(@RequestHeader("Authorization") String authHeader){
        var token = authHeader.replace("Bearer ", "");
        var jwt = jwtService.parseToken(token);
        return jwt != null && !jwt.isExpired();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> loginUser(){
        var context  =  SecurityContextHolder.getContext().getAuthentication();
        String id = context.getPrincipal().toString();
        var user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        var userDto = userMapper.toDto(user); 
        return ResponseEntity.ok(userDto);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
