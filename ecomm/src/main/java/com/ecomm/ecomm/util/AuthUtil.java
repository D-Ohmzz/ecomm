package com.ecomm.ecomm.util;

import com.ecomm.ecomm.model.User;
import com.ecomm.ecomm.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthUtil {
    private UserRepository userRepository;

    public AuthUtil(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    //Getting email
    public String loggedInEmail(){
        // Authentication object instance with details of the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found with username: "+ authentication.getName());
        }
        else{
            return user.get().getEmail();
        }
    }

    public Long loggedInUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found with username: "+ authentication.getName());
        }
        else{
            return user.get().getId();
        }
    }

    public User loggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found with username: "+ authentication.getName());
        }
        else{
            return user.get();
        }
    }





}
