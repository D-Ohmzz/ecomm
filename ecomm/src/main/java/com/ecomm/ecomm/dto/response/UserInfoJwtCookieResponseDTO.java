package com.ecomm.ecomm.dto.response;

import java.util.List;

public record UserInfoJwtCookieResponseDTO(
        Long id,
        String username,
        List<String> roles,
        String cookie
) {
}
