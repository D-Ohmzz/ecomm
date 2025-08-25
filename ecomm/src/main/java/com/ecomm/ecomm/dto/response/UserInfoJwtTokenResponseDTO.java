package com.ecomm.ecomm.dto.response;

import java.util.List;

public record UserInfoResponseDTO(
        Long id,
        String username,
        List<String> roles,
        String jwtToken
){}
