package com.example.demo.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepo;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final UserRepo userRepository;

    public MyUserDetailService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isPresent()){
            throw new UsernameNotFoundException("User not found with email"+email);
        }
        else{
            return org.springframework.security.core.userdetails.User.builder()
            .username(user.get().getUsername())
            .password(user.get().getPassword())
            .roles(user.get().getRole()) 
            .disabled(!user.get().isActive())
            .build();

        }
    }

}
