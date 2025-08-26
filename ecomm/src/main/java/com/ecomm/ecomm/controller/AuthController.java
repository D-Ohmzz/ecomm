package com.ecomm.ecomm.controller;

import com.ecomm.ecomm.dto.request.SigninRequestDTO;
import com.ecomm.ecomm.dto.request.SignupRequestDTO;
import com.ecomm.ecomm.dto.response.UserInfoJwtCookieResponseDTO;
import com.ecomm.ecomm.dto.response.UserInfoJwtTokenResponseDTO;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
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
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // UserInfoJwtTokenResponseDTO userInfoJwtTokenResponseDTO = new UserInfoJwtTokenResponseDTO(userDetails.getId(),
        //        userDetails.getUsername(), roles, jwtToken);
        UserInfoJwtCookieResponseDTO userInfoJwtCookieResponseDTO = new UserInfoJwtCookieResponseDTO(userDetails.getId(),
                userDetails.getUsername(), roles, jwtCookie.toString());
        //return new ResponseEntity<>(userInfoJwtTokenResponseDTO, HttpStatus.OK);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                jwtCookie.toString())
                .body(userInfoJwtCookieResponseDTO);
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequestDTO signupRequestDTO){
        User user = new User(
                signupRequestDTO.username(),
                signupRequestDTO.email(),
                passwordEncoder.encode(signupRequestDTO.password())
        );
        Set<String> strRoles  = signupRequestDTO.roles();
        Set<Role> roles = new HashSet<>();
        if(userRepository.existsByUsername(signupRequestDTO.username())){
            throw new APIException("The submitted username {"+ signupRequestDTO.username()+"} has been taken please create a new one!!!");
        }

        if(userRepository.existsByEmail(signupRequestDTO.email())){
            throw new APIException("The provide email email {"+ signupRequestDTO.username()+"} already exists!!!");
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
        Map<String, Object> map = new HashMap<>();
        map.put("message", "User registered successfully!!!");
        map.put("status", "true");
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping("/username")
    public String getUsernameOfSignedInUser(Authentication authentication){
        if(authentication != null ){
            return authentication.getName();
        }else{
            return "";
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getUserDetailsOfSignedInUser(Authentication authentication){
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        UserInfoResponseDTO userInfoResponseDTO = new UserInfoResponseDTO(userDetails.getId(),
                userDetails.getUsername(), roles);
        return ResponseEntity.ok()
                .body(userInfoResponseDTO);
    }

    @PostMapping("/signout")
    public ResponseEntity<Object>signoutUser(){
        ResponseCookie cookie  = jwtUtils.generateCleanJwtCookie();
        Map<String, String> map = new HashMap<>();
        map.put("message","You've been signed out !!!");
        map.put("status", "true");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        cookie.toString())
                .body(map);
    }
}
