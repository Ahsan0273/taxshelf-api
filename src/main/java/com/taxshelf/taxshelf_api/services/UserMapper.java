package com.taxshelf.taxshelf_api.services;

import org.springframework.stereotype.Component;

import com.taxshelf.taxshelf_api.dtos.RegisterUserRequest;
import com.taxshelf.taxshelf_api.dtos.UserDto;
import com.taxshelf.taxshelf_api.entities.User;

@Component
public class UserMapper {
    public UserDto toDto(User user){
        return new UserDto(user.getId(), user.getUserName(), user.getEmail());
    }
    public User toEntity(RegisterUserRequest user) {
        return User.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword()) // don’t forget password!
                .build();
    }
    }
