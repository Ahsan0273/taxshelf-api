package com.taxshelf.taxshelf_api.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.taxshelf.taxshelf_api.dtos.RegisterUserRequest;
import com.taxshelf.taxshelf_api.enums.RoleType;
import com.taxshelf.taxshelf_api.repositories.RoleRepository;
import com.taxshelf.taxshelf_api.repositories.UserRepository;
import com.taxshelf.taxshelf_api.services.UserMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<?> registerUser(
        @Valid @RequestBody RegisterUserRequest request,
        UriComponentsBuilder uriBuilder){

        if(userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body(
                Map.of("email", "Email is already registered")
            );
        }

        var role = roleRepository.findByName(RoleType.ADMIN.toString()).orElse(null);

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        userRepository.save(user);

        var userDto = userMapper.toDto(user);
        var uri = uriBuilder.path("/user/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }
}
