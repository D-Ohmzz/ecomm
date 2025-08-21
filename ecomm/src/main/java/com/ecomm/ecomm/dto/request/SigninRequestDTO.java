package com.ecomm.ecomm.dto.request;

import jakarta.validation.constraints.NotBlank;
public record SigninRequestDTO(
        @NotBlank(message="Username field cannot be blank !!!")
        String username,
        @NotBlank(message="Password field cannot be blank !!!")
        String password
){}
