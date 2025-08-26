package com.ecomm.ecomm.controller;

import com.ecomm.ecomm.dto.request.SigninRequestDTO;
import com.ecomm.ecomm.dto.request.SignupRequestDTO;
import com.ecomm.ecomm.dto.response.UserInfoJwtCookieResponseDTO;
import com.ecomm.ecomm.dto.response.UserInfoResponseDTO;
import com.ecomm.ecomm.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService=authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@RequestBody SigninRequestDTO signinRequestDTO){
        UserInfoJwtCookieResponseDTO userInfoJwtCookieResponseDTO = authService.signinUser(signinRequestDTO);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                userInfoJwtCookieResponseDTO.cookie())
                .body(userInfoJwtCookieResponseDTO);
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequestDTO signupRequestDTO){
        authService.registerUser(signupRequestDTO);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "User registered successfully!!!");
        map.put("status", "true");
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping("/username")
    public String getUsernameOfSignedInUser(Authentication authentication){
        return authService.getUsername(authentication);
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getUserDetailsOfSignedInUser(Authentication authentication){
        UserInfoResponseDTO userInfoResponseDTO = authService.getUserDetails(authentication);
        return ResponseEntity.ok()
                .body(userInfoResponseDTO);
    }

    @PostMapping("/signout")
    public ResponseEntity<Object>signoutUser(){
        ResponseCookie cookie = authService.signoutUser();
        Map<String, String> map = new HashMap<>();
        map.put("message","You've been signed out !!!");
        map.put("status", "true");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        cookie.toString())
                .body(map);
    }
}
