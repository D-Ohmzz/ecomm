package com.ecomm.ecomm.service.implementation;

import com.ecomm.ecomm.dto.request.SigninRequestDTO;
import com.ecomm.ecomm.dto.request.SignupRequestDTO;
import com.ecomm.ecomm.dto.response.UserInfoJwtCookieResponseDTO;
import com.ecomm.ecomm.dto.response.UserInfoResponseDTO;
import com.ecomm.ecomm.exceptions.APIException;
import com.ecomm.ecomm.model.AppRole;
import com.ecomm.ecomm.model.Role;
import com.ecomm.ecomm.model.User;
import com.ecomm.ecomm.repository.RoleRepository;
import com.ecomm.ecomm.repository.UserRepository;
import com.ecomm.ecomm.security.jwt.JwtUtils;
import com.ecomm.ecomm.security.services.UserDetailsImplementation;
import com.ecomm.ecomm.service.AuthService;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImplementation implements AuthService {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    public AuthServiceImplementation(JwtUtils jwtUtils, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, AuthenticationManager authenticationManager){
        this.jwtUtils=jwtUtils;
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.roleRepository=roleRepository;
        this.authenticationManager=authenticationManager;
    }

    @Override
    public ResponseCookie signoutUser() {
        return jwtUtils.generateCleanJwtCookie();
    }

    @Override
    public UserInfoResponseDTO getUserDetails(Authentication authentication) {
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new UserInfoResponseDTO(userDetails.getId(),
                userDetails.getUsername(), roles);
    }

    @Override
    public String getUsername(Authentication authentication) {
        if(authentication != null ){
            return authentication.getName();
        }else{
            return "";
        }
    }

    @Override
    public void registerUser(SignupRequestDTO signupRequestDTO) {
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
    }

    @Override
    public UserInfoJwtCookieResponseDTO signinUser(SigninRequestDTO signinRequestDTO) {
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
            throw new APIException("Invalid credentials !!!");
        }
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // return new UserInfoJwtTokenResponseDTO(userDetails.getId(),
        //        userDetails.getUsername(), roles, jwtToken);
        return new UserInfoJwtCookieResponseDTO(userDetails.getId(),
                userDetails.getUsername(), roles, jwtCookie.toString());
    }
}
