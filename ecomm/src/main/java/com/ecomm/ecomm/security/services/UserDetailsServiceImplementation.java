package com.ecomm.ecomm.security.services;

import com.ecomm.ecomm.model.User;
import com.ecomm.ecomm.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {
    private final UserRepository userRepository;
    public UserDetailsServiceImplementation(UserRepository userRepository){
        this.userRepository=userRepository;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->
                        new UsernameNotFoundException("User Not Found with username: "+username));
        return UserDetailsImplementation.build(user);
    }
}
