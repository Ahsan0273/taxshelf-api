package com.taxshelf.taxshelf_api.dtos;

import lombok.Data;

@Data
public class RegisterUserRequest {

    private String email;
    private String userName;
    private String password; 
}
