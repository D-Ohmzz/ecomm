package com.ecomm.ecomm.controller;

import com.ecomm.ecomm.dto.request.SigninRequestDTO;
import com.ecomm.ecomm.dto.response.UserInfoResponseDTO;
import com.ecomm.ecomm.security.jwt.JwtUtils;
import com.ecomm.ecomm.security.services.UserDetailsImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthController {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    public AuthController(JwtUtils jwtUtils, AuthenticationManager authenticationManager){
        this.jwtUtils=jwtUtils;
        this.authenticationManager=authenticationManager;
    }
    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@RequestBody SigninRequestDTO signinRequestDTO){
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signinRequestDTO.username(),
                            signinRequestDTO.password()
                    )
            );

        }catch (AuthenticationException exception){
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad Credentials");
            map.put("status", "false");
            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        }
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        UserInfoResponseDTO userInfoResponseDTO = new UserInfoResponseDTO(userDetails.getId(), userDetails.getUsername(), roles, jwtToken);
        return new ResponseEntity<>(userInfoResponseDTO, HttpStatus.OK);
    }
}
