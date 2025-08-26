package com.ecomm.ecomm.service;

import com.ecomm.ecomm.dto.request.SigninRequestDTO;
import com.ecomm.ecomm.dto.request.SignupRequestDTO;
import com.ecomm.ecomm.dto.response.UserInfoJwtCookieResponseDTO;
import com.ecomm.ecomm.dto.response.UserInfoResponseDTO;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;

public interface AuthService {
    ResponseCookie signoutUser();
    UserInfoResponseDTO getUserDetails(Authentication authentication);
    String getUsername(Authentication authentication);
    void registerUser(SignupRequestDTO signupRequestDTO);
    UserInfoJwtCookieResponseDTO signinUser(SigninRequestDTO signinRequestDTO);
}
