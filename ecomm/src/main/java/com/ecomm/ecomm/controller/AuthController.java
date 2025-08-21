package com.ecomm.ecomm.controller;

import com.ecomm.ecomm.dto.request.SigninRequestDTO;
import com.ecomm.ecomm.dto.request.SignupRequestDTO;
import com.ecomm.ecomm.dto.response.UserInfoResponseDTO;
import com.ecomm.ecomm.exceptions.APIException;
import com.ecomm.ecomm.model.AppRole;
import com.ecomm.ecomm.model.Role;
import com.ecomm.ecomm.model.User;
import com.ecomm.ecomm.repository.RoleRepository;
import com.ecomm.ecomm.repository.UserRepository;
import com.ecomm.ecomm.security.jwt.JwtUtils;
import com.ecomm.ecomm.security.services.UserDetailsImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

public class AuthController {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    public AuthController(JwtUtils jwtUtils, AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository){
        this.jwtUtils=jwtUtils;
        this.authenticationManager=authenticationManager;
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.roleRepository=roleRepository;
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

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDTO signupRequestDTO){
        User user = new User(
                signupRequestDTO.username(),
                signupRequestDTO.email(),
                passwordEncoder.encode(signupRequestDTO.password())
        );
        Set<String> strRoles  = signupRequestDTO.roles();
        Set<Role> roles = new HashSet<>();
        if(!userRepository.existsByUsername(signupRequestDTO.username())){

        }
        else{
            throw new APIException("The submitted username {"+ signupRequestDTO.username()+"} has been taken please create a new one!!!");
        }
        if(!userRepository.existsByEmail(signupRequestDTO.email())){

        }
        else{
            throw new APIException("THe provide email email {"+ signupRequestDTO.username()+"} already exists!!!");
        }
        if(strRoles == null){
            //when user did not send a role, assign a default role
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(()-> new RuntimeException("Error: Role is Not Found"));
            roles.add(userRole);
        }else{
            //admin --> ROLE_ADMIN
            //seller -->ROLE_SELLER
            strRoles.forEach(role->{
                switch(role){
                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                .orElseThrow(()-> new RuntimeException("Error: Role is Not Found"));
                        roles.add(sellerRole);
                        break;
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(()-> new RuntimeException("Error: Role is Not Found"));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(()-> new RuntimeException("Error: Role is Not Found"));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return new ResponseEntity<>(" User registered successfully!!!", HttpStatus.CREATED);


    }
}
