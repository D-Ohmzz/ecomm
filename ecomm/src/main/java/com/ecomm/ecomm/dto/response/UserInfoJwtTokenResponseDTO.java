package com.ecomm.ecomm.dto.response;

import java.util.List;

public record UserInfoJwtTokenResponseDTO(
        Long id,
        String username,
        List<String> roles,
        String jwtToken
){}
