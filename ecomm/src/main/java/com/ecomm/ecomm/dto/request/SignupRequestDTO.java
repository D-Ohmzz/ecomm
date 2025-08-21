package com.ecomm.ecomm.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record SignupRequestDTO(
        @NotBlank(message="Username field cannot be blank !!!")
        @Size(min = 3, max = 15)
        String username,
        @NotBlank(message="Email field cannot be blank !!!")
        @Size(max = 50)
        @Email
        String email,
        @NotBlank(message="Password field cannot be blank !!!")
        @Size(min = 5, max = 25)
        String password,
        Set<String> roles

){}
